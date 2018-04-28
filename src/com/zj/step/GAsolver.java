package com.zj.step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.lagodiuk.ga.Chromosome;
import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.IterartionListener;
import com.lagodiuk.ga.Population;
import com.zj.information.Info;
import com.zj.information.VM;
import com.zj.solution.PMsolu;
import com.zj.solution.VMsolu;



public class GAsolver {

	private PMsolu pmsolu;

	private Info info;

	private GA_MarkList mark;

	private long startTime;

	public GAsolver(PMsolu pmsolu, Info info, long startTime) {

		this.pmsolu = pmsolu;
		this.info = info;
		this.startTime = startTime;
		mark = new GA_MarkList(pmsolu);
		//mark = new Mark(vmsolu);

		//System.out.println("\n\n" + mark.toString() + "\n\n");

	}

	public PMsolu run() {

		Population<MyVector> population = createInitialPopulation(40);

		Fitness<MyVector, PMsolu> fitness = new MyVectorFitness();

		GeneticAlgorithm<MyVector, PMsolu> ga = new GeneticAlgorithm<MyVector, PMsolu>(population, fitness);

		addListener(ga);

		ga.evolve(1500);


		MyVector best = ga.getBest();
		PMsolu bestPMsolu = ga.fitness(best);
		return bestPMsolu;
	}

	/**
	 * 初始化基因染色体，染色体数组1按放入箱子的顺序编号，编号范围0 ~ numVM-1；
	 * 染色体数组2按箱子的种类索引编号，编号范围0 ~ 2，数组大小pmsolu.num
	 * 首次编号为FFD放置的顺序
	 */
	private Population<MyVector> createInitialPopulation(int populationSize) {
		Population<MyVector> population = new Population<MyVector>();
		MyVector base = new MyVector();
		//初始化染色体数组
		base.getVector()[0] = new int[mark.numMark()];
		for (int i = 0; i < mark.numMark(); i++) {
			base.getVector()[0][i] = i;
		}
		base.getVector()[1] = new int[pmsolu.numPMsolu()];
		for (int i = 0; i < pmsolu.numPMsolu(); i++) {
			base.getVector()[1][i] = pmsolu.getPMsolu(i).getPlacedPM().getIndex();
		}

		for (int i = 0; i < populationSize; i++) {
			// each member of initial population
			// is mutated clone of base chromosome
			MyVector chr = base.mutate();
			population.addChromosome(chr);
		}
		return population;
	}

	/**
	 * After each iteration Genetic algorithm notifies listener
	 */
	private void addListener(GeneticAlgorithm<MyVector, PMsolu> ga) {
		// just for pretty print
		System.out.println(String.format("%s\t%s\t%s", "iter", "fit", "chromosome"));

		// Lets add listener, which prints best chromosome after each iteration
		ga.addIterationListener(new IterartionListener<MyVector, PMsolu>() {

			private final double threshold = 1e-5;
			int[] a = new int[1500];

			@Override
			public void update(GeneticAlgorithm<MyVector, PMsolu> ga) {

				MyVector best = ga.getBest();
				PMsolu bestPMsolu = ga.fitness(best);
				int iteration = ga.getIteration();

				// Listener prints best achieved solution
				System.out.println(String.format("%s\t%s:%s\t%s", iteration, bestPMsolu.numPMsolu(),bestPMsolu.getUtilization(), best));

				// If fitness is satisfying - we can stop Genetic algorithm


				long endTime = System.currentTimeMillis();
				long during =  (endTime - startTime) / 1000;
				if (during > 85 || bestPMsolu.getUtilization() < this.threshold) {
					ga.terminate();
				}

			}
		});
	}

	/**
	 * 染色体
	 */
	public class MyVector implements Chromosome<MyVector>, Cloneable {

		private final Random random = new Random();

		private final int[][] vector = new int[2][];
		//private final int[][] vector = new int[3][];

		/**
		 * 变异
		 */
		@Override
		public MyVector mutate() {
			MyVector result = this.clone();

			/**
			 * 多点变异数组1
			 */
			for (int i = 0; i < 3 ; i++) {
				int index1_1 = random.nextInt(this.vector[0].length);
				int index1_2 = random.nextInt(this.vector[0].length);
				//交换
				int temp = result.vector[0][index1_1];
				result.vector[0][index1_1] = result.vector[0][index1_2];
				result.vector[0][index1_2] = temp;
			}

			/**
			 * 多点变异数组2
			 */
			for (int i = 0; i < 2 ; i++) {
				result.vector[1][random.nextInt(this.vector[1].length)] = random.nextInt(3);
			}
			return result;
		}

		/**
		 * 交叉进化
		 */
		@Override
		public List<MyVector> crossover(MyVector other) {

			MyVector thisClone;
			MyVector otherClone;
			// 单点交叉
			// other——>this
			//thisClone = sortBySingle(this, other);
			// this——>other
			//otherClone = sortBySingle(other, this);

			//多点交叉
			thisClone = this.clone();
			otherClone = other.clone();

			sortByMultiple(thisClone, otherClone);


			return Arrays.asList(thisClone, otherClone);
		}

		/**
		 * slaver ——> master: slaver复制到mater的后段，并且编号不能重复
		 * @param master
		 * @param slave
		 * @param index
		 * @return
		 */
		private MyVector sortBySingle(MyVector master, MyVector slave) {


			MyVector masterClone = master.clone();
			//对数组1操作
			int index1 = random.nextInt(this.vector[0].length - 1);
			ArrayList<Integer> thisRaw = new ArrayList<>();
			for (int i = 0; i < index1 ; i++) {
				thisRaw.add(master.vector[0][i]);
			}

			int k = index1;
			for (int i = 0; i < slave.vector[0].length; i++) {
				boolean isEqual = false;
				for (int j = 0; j < thisRaw.size(); j++) {
					if (thisRaw.get(j) == slave.vector[0][i]) {
						thisRaw.remove(j);
						isEqual = true;
						break;
					}
				}
				if (!isEqual) {
					masterClone.vector[0][k++] = slave.vector[0][i];
				}
			}
			//对数组2操作
			int index2 = random.nextInt(this.vector[1].length - 1);
			for (int i = index2; i < slave.vector[1].length; i++) {
				masterClone.vector[1][i] = slave.vector[1][i];
			}

			return masterClone;

		}


		private void sortByMultiple(MyVector masterClone, MyVector slaveClone) {

			//两组染色体数组
			for (int k = 0; k < masterClone.vector.length; k++) {

				//每个基因被保留的几率
				boolean[] masterSelect = new boolean[masterClone.vector[k].length];
				boolean[] slaveSelect = new boolean[slaveClone.vector[k].length];

				for (int i = 0; i < masterSelect.length; i++) {
					//70%的概率，非0代表选中保留
					masterSelect[i] = random.nextInt(10) > 5 ? true : false;
					if (masterSelect[i]) {
						//选择的记录
						for (int j = 0; j < slaveClone.vector[k].length; j++) {
							if (slaveClone.vector[k][j] == masterClone.vector[k][i]) {
								slaveSelect[j] = true;
								break;
							}
						}
					}
				}

				int index = 0;
				for (int i = 0; i < masterClone.vector[k].length; i++) {
					//选择pass
					if (masterSelect[i]) {
						continue;
					}
					while (index++ <= masterClone.vector[k].length) {
						if (!slaveSelect[index-1]) {
							//替换
						int temp = masterClone.vector[k][i];
						masterClone.vector[k][i] = slaveClone.vector[k][index-1];
						slaveClone.vector[k][index-1] = temp;
						break;
						}


					}
				}

			}

		}



		@Override
		protected MyVector clone() {
			MyVector clone = new MyVector();
			for (int i = 0; i < this.vector.length; i++) {
				clone.getVector()[i] = new int[vector[i].length];
				for (int j = 0; j < vector[i].length; j++) {
					clone.vector[i][j] = this.vector[i][j];
				}
			}
			return clone;
		}

		public int[][] getVector() {
			return this.vector;
		}

		@Override
		public String toString() {
			return "{" + Arrays.toString(this.vector[0])  +
					"}{" + Arrays.toString(this.vector[1]) + "}";
		}
	}

	/**
	 * 适应度函数，目标越小越好
	 */
	public class MyVectorFitness implements Fitness<MyVector, PMsolu> {

		@Override
		public PMsolu calculate(MyVector chromosome) {

			//开启指定类型物理服务器
			int indexPM = 0;
			PMsolu pre_pmsolu = new PMsolu(info,info.getPMs().getPM(chromosome.vector[1][indexPM]));
			for (int i = 0; i < chromosome.vector[0].length; i++) {

				VM vm = mark.getMark(chromosome.vector[0][i]);

				//放置成功标志
				boolean flag = false;
				for (int j = 0; j <= indexPM; j++) {
					int idelCPUcapacity = pre_pmsolu.getPMsolu(j).getIdleCPU();
					int idelMEMcapacity = pre_pmsolu.getPMsolu(j).getIdleMEM();
					if (vm.getCPU() <= idelCPUcapacity && vm.getMEM() <= idelMEMcapacity) {
						pre_pmsolu.addPMsolu(j, vm, 1);
							flag = true;
							break;
					}
				}
				//开辟新物理节点
				if (flag == false) {
					if (indexPM <= chromosome.vector[1].length -1) {
						pre_pmsolu.addNewPMsolu(info.getPMs().getPM(chromosome.vector[1][indexPM]));
					}else {
						pre_pmsolu.addNewPMsolu();
					}
					indexPM++;
					pre_pmsolu.addPMsolu(indexPM, vm, 1);
				}
			}
			//System.out.println(pre_pmsolu.toString());
			return pre_pmsolu;

		}
	}

}
