package aicw2;

import java.util.*;
import java.io.*;


public class EuclidDistance {

	public static int myData1[][] = new int[2810][65];
	public static int myData2[][] = new int[2810][65];
	
	
	public static void main(String[] args) {
		String filePath1 = "cw2DataSet1.csv";
		String filePath2 = "cw2DataSet2.csv";
		myData1 = readFile(filePath1,myData1);
		myData2 = readFile(filePath2,myData2);
		Set1onSet2(myData1,myData2);
		Set1onSet2(myData2,myData1);
	}
	public static void Set1onSet2(int[][] data1, int[][]data2)
	{
		int numOfCorrect = 0;
		int prediction = -1;
		double distance = Double.MAX_VALUE;
		for(int row1= 0; row1<2810; row1++)
		{	double dist = 0;			
			for(int row2= 20; row2<2810; row2++)
			{
				for(int col=0; col<64; col++)
				{
					dist +=(data1[row1][col] - data2[row2][col])*(data1[row1][col] - data2[row2][col]) ;
					
				}
				dist = Math.sqrt(dist);
				if(distance > dist)
				{
					distance = dist;
					prediction = data2[row2][64];
					
				}
			}
			if(prediction == data1[row1][64])
			{
				numOfCorrect++;
			}
			else {
			}
			prediction=-1;
			distance = Double.MAX_VALUE;
		}		
		double acc = (double) numOfCorrect/2810;
		System.out.println("Accuracy = " + acc*100);
	}
	public static int[][] readFile(String filePath, int[][]myData)
	{
		String Filepath=filePath;
		Scanner sc1 = null;
		try {
			File obj = new File(Filepath);
			sc1 = new Scanner(obj);
		}
		catch(Exception ex){
			System.out.println("File not Found..!!!");
			System.out.println("File path is hard coded please change File location..!!!");
		}
		int i=0;
		while(sc1.hasNextLine())
		{
			String data = sc1.nextLine();
			String[] line = data.split(",");
			for(int l=0; l<line.length;l++)
			myData[i][l]= Integer.parseInt(line[l]);
			i++;
		}
		return myData;
	}

}
