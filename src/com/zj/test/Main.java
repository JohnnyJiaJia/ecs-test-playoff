package com.zj.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main {

	final static String inputfilepath = "C:\\Users\\PC\\Desktop\\练习数据 (1)\\复赛文档\\用例示例\\input_all.txt";
	//final static String inputfilepath = "C:\\Users\\PC\\Desktop\\练习数据 (1)\\复赛文档\\用例示例\\input_3hosttypes_5flavors_1week.txt";

	final static String trainfilepath = "C:\\Users\\PC\\Desktop\\练习数据 (1)\\复赛文档\\用例示例\\TrainData_2015.12.txt";
	//final static String trainfilepath = "C:\\Users\\PC\\Desktop\\练习数据 (1)\\复赛文档\\用例示例\\data_678.txt";


	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();    //获取开始时间

		Predict.predictVm(readFile(trainfilepath), readFile(inputfilepath));

		long endTime = System.currentTimeMillis();    //获取结束时间

		System.out.println("\n程序运行时间：" + (endTime - startTime) + "ms");
	}

	/**
	 * 读取文件
	 * @param filename
	 * @return
	 */
	public static String[] readFile(String filename) {

		String encoding = "UTF-8";
		File file = new File(filename);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String filestring = new String(filecontent, encoding);
			return filestring.split("\r\n|\n");

		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

}
