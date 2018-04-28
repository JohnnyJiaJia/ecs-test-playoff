package com.zj.step;
import com.hjk.lstm.Network;
import com.hjk.matrix.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hjk.data.Data;


//import com.hjk.lstm.Lstm;
public class LSTMsolver {



		/**
         * input: data:         一种flavor的历史请求数量
         *        day_interval: 间隔天数
         *        day_pre:      预测天数
         * output:pre_vm:       预测期间的虚拟机数量
         */
    public static int run(int[] data, int day_interval, int day_pre){

        // 构造训练样本， data: 为一种flavor的历史请求数量
        int step = day_interval + day_pre;
        Data train = new Data(data, step);
        Matrix[][] train_x = train.getTrain_x();
        Matrix[] train_y = train.getTrain_y();

         // 初始化LSTM
        Network lstm = new Network(1, 4);

        // 训练LSTM， lr：学习速率 epochs: 迭代次数
        lstm.train(train_x, train_y, 8, 1, 55);


        // 滚动预测
        Matrix[] pre_arr = new Matrix[day_interval + day_pre];
        Matrix[] pre_x = train.getPre_x();
        Matrix pre_y = lstm.predict(pre_x);
        pre_arr[0] = pre_y;
        for (int i = 1; i < day_interval + day_pre; i++) {
            pre_x = train.nextPre_x(pre_y);
            pre_y = lstm.predict(pre_x);
            pre_arr[i] = pre_y;
        }

        // pre_vm， 预测区间的虚拟机数量
        double pre_vm = 0;
        for (int i = day_interval; i < day_interval + day_pre; i++){
            double value = pre_arr[i].get(0,0);
            value = train.normalize_re(value);
            value = Math.round(value);
            if (value < 0){
                value = 0;
            }
            pre_vm += value;

        }

        BigDecimal b = new  BigDecimal(pre_vm);
		int c = b.setScale(0, RoundingMode.HALF_UP).intValue();

        System.out.printf("predict vm numbers: %d\n", c);

        return c;
    }

}

