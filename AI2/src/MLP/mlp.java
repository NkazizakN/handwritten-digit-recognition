package MLP;
import java.util.*;
import java.io.*;

public class mlp {

	public static int myData1[][] = new int[2810][65];
	public static int myData2[][] = new int[2810][65];
	
	public static void main(String[] args) 
	{
		String filePath1 = "cw2DataSet1.csv";
		String filePath2 = "cw2DataSet2.csv";
		myData1 = readFile(filePath1,myData1);
		myData2 = readFile(filePath2,myData2);
		NueralNetwork nn1 = new NueralNetwork();
		NueralNetwork nn2 = new NueralNetwork();
		nn1.Setup(0.05,50,50,10);
		nn2.Setup(0.05,50,50,10);
		System.out.println("\nTraining with Dataset1 and testing on Dataset 2....");
		nn1 = trainNetwork(myData1,nn1);
		test(myData2,nn1);
		System.out.println("\nTraining with Dataset2 and testing on Dataset 1....");
		nn2 = trainNetwork(myData2,nn2);
		test(myData1,nn2);
	}
	public static void test(int[][] data, NueralNetwork n)
	{
		int correctGuess = 0;
			for(int i=0;i<data.length;i++)
			{
				double[] test_inputs = new double[64];
				for(int j=0;j<64;j++)
				{
					test_inputs[j] = (double)data[i][j];
				}
				int target = data[i][64];
				n.feedforward(test_inputs);
				int x = n.getOutput();
				if(x == target)
				{
					correctGuess = correctGuess + 1;
				}
				
			}
			double accuracy = (double) correctGuess/data.length;
			accuracy = accuracy*100;
			System.out.println("Accuracy = "+accuracy + "%");

	}
	public static NueralNetwork trainNetwork(int[][] data1, NueralNetwork n)
	{
		for(int l=0; l<100; l++)
		{
			//System.out.println(l);
			for(int i=0;i<data1.length;i++)
			{
				double[] inputs = new double[64];
				for(int j=0;j<64;j++)
				{
					inputs[j] = (double)data1[i][j];
				}
				double[] targets = new double[10];
				//creating target vector
				for(int k=0;k<10;k++)
				{
					if(data1[i][64] == k)
					{
						targets[k]=0.99;
					}
					else
					{
						targets[k]=0.01;
					}
				}
				n.train(inputs, targets);
			}
		}
		return n;
	}
	//reading data from FIle
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
