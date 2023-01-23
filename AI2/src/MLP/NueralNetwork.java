package MLP;

public class NueralNetwork {

	double[] H_layer1;
	double[] H_layer2;
	double[] O_layer;
	
	double[][] I_H_weights;
	double[][] H_H_weights;
	double[][] H_O_weights;
	
	double[] O_errors;
	double[] H2_errors;
	double[] H1_errors;
	
	double learning_rate;
	
	//Setup of the network
	void Setup(double lr, int h1, int h2, int o)
	{
		//initialising the layers
		this.H_layer1 = new double[h1];
		this.H_layer2 = new double[h2];
		this.O_layer = new double[o];
		
		//initialising the error arrays
		this.O_errors = new double[o];
		this.H2_errors = new double[h2];
		this.H1_errors = new double[h1];
		
		//initialising and randomising the weights
		this.I_H_weights = new double[h1][64];
		this.I_H_weights = assignRandom(this.I_H_weights, h1, 64);
		this.H_H_weights = new double[h2][h1];
		this.H_H_weights = assignRandom(this.H_H_weights,h2,h1);
		this.H_O_weights = new double[o][h2];
		this.H_O_weights = assignRandom(this.H_O_weights,o,h2);
		this.learning_rate = lr;
	}
	
	//assign random value to any any array passed as parameter
	double[][] assignRandom(double[][] arr, int rows, int cols)
	{
		for(int row=0; row<rows; row++)
		{
			for(int col=0; col<cols; col++)
			{
				//initialising the weights between -1 and 1
				arr[row][col] = (Math.random()*2) - 1;
			}
		}
		return arr;
	}
	
	void train(double[] inputs, double[] targets)
	{
		feedforward(inputs);
		backPropagation(targets,inputs);
		//cost function
	}
	
	/* ****************
	/back propagation starts here
	 * ********************/
	
	void backPropagation(double[] targets,double[] inputs)
	{
		//hidden->output
		this.O_errors = elementwise_substract(this.O_layer, targets);
		double[] O_layer_desigmoid = desigmoid(this.O_layer);
		double[] gradient1= elementwise_Multiplication(this.O_errors,O_layer_desigmoid);
		//gradient1 = dotProduct();
		double[][] delta_weights_HO = multiply(gradient1,this.H_layer2);
		delta_weights_HO = scalar_multiply(delta_weights_HO,this.learning_rate);
		
		//Hidden->hidden
		double[][] H_O_weights_transpose = transpose(this.H_O_weights);
		double[] desigmoid_H2_layer = desigmoid(this.H_layer2);
		double[] gradient2 = dotProduct(H_O_weights_transpose,gradient1);
		gradient2 = elementwise_Multiplication(gradient2,desigmoid_H2_layer);
		double[][] delta_weights_HH = multiply(gradient2,this.H_layer1);
		delta_weights_HH = scalar_multiply(delta_weights_HH,this.learning_rate);
		//Input->Hidden
		double[][] H_H_weights_transpose = transpose(this.H_H_weights);
		double[] gradient3 = dotProduct(H_H_weights_transpose,gradient2);
		double[] desigmoid_H1_layer = desigmoid(this.H_layer1);
		gradient3 = elementwise_Multiplication(gradient3,desigmoid_H1_layer);
		double[][] delta_weights_IH = multiply(gradient3,inputs);
		delta_weights_IH = scalar_multiply(delta_weights_IH,this.learning_rate);
		
		//updating the weights
		this.H_O_weights = elementwise_substract_mat(this.H_O_weights,delta_weights_HO);
		this.H_H_weights = elementwise_substract_mat(this.H_H_weights,delta_weights_HH);
		this.I_H_weights = elementwise_substract_mat(this.I_H_weights,delta_weights_IH);
	}
	double[][] scalar_multiply(double[][] matrix, double x)
	{
		for(int index1=0;index1<matrix.length;index1++)
		{
			for(int index2=0;index2<matrix[0].length;index2++)
			{
				matrix[index1][index2] = matrix[index1][index2] * x;
			}
		}
		return matrix;
	}
	double[][] multiply(double[] matrix1, double[] matrix2)
	{
		double[][] mat = new double[matrix1.length][matrix2.length];
		for(int index1=0;index1<matrix1.length;index1++)
		{
			for(int index2=0;index2<matrix2.length;index2++)
			{
				mat[index1][index2] = matrix1[index1]*matrix2[index2];
			}
		}
		return mat;
	}
	double[] elementwise_Multiplication(double[] matrix1, double[] matrix2)
	{
		if(matrix1.length != matrix2.length)
		{
			System.out.println("dimension error in elementwise multiplication");
		}
		else
		{
			for(int index=0; index<matrix1.length;index++)
			{
				matrix1[index] = matrix1[index] * matrix2[index]; 
			}
		}
		return matrix1;
	}
	double[] dotProduct(double[][] weights, double[] layer)
	{
		double[] matrix = new double[weights.length];
		if(weights[0].length != layer.length)
		{
			System.out.println("error in dimensions (insode dot product function)");
		}
		else
		{
			for(int index1=0; index1<weights.length;index1++)
			{
				double sum = 0;
				for(int index2 =0; index2<weights[0].length;index2++)
				{
					sum = sum + (weights[index1][index2] * layer[index2]);
				}
				matrix[index1] = sum;
			}
		}
		return matrix;
	}
	double[] desigmoid(double[] matrix)
	{
		double[] mat = new double[matrix.length];
		for(int index=0; index<matrix.length;index++)
		{
			mat[index] = matrix[index] *(1 - matrix[index]);
		}
		return mat;
	}
	
	double[][] transpose(double[][] matrix)
	{
		double[][] mat = new double[matrix[0].length][matrix.length];
		for(int index1=0; index1<matrix.length;index1++)
		{
			for(int index2=0; index2<matrix[0].length; index2++)
			{
				mat[index2][index1] = matrix[index1][index2];
			}
		}
		return mat;
	}
	
	double[] elementwise_substract(double[] matrix1, double[] matrix2)
	{
		double[] matrix = new double[matrix1.length];
		if(matrix1.length != matrix2.length)
		{
			System.out.println("Matrix substract not posiible inccorect dimensions");
		}
		else 
		{
			for(int index=0; index<matrix1.length;index++)
			{
				matrix[index] = matrix1[index] - matrix2[index];
			}
		}
		return matrix;
	}
	double[][] elementwise_substract_mat(double[][] matrix1, double[][] matrix2)
	{
		double[][] matrix = new double[matrix1.length][matrix1[0].length];
		if(matrix1.length != matrix2.length)
		{
			System.out.println("Matrix substract not posiible inccorect dimensions");
		}
		else 
		{
			for(int index=0; index<matrix1.length;index++)
			{
				for(int index2=0; index2<matrix1.length;index2++)
				{
					matrix[index][index2] = matrix1[index][index2] - matrix2[index][index2];
				}
				
			}
		}
		return matrix;
	}
	
	
	/* ************
	/feed forward starts here
	************* */
	void feedforward(double[] inputs) 
	{
		this.H_layer1 = weighted_sum(I_H_weights, inputs);
		this.H_layer1 = activation(this.H_layer1);
		this.H_layer2 = weighted_sum(H_H_weights, this.H_layer1);
		this.H_layer2 = activation(this.H_layer2);
		this.O_layer = weighted_sum(H_O_weights, this.H_layer2);
		this.O_layer = activation(this.O_layer);
	}
	
	double[] weighted_sum(double[][] weights, double[] neurons)
	{
		double[] vector = new double[weights.length];
		for(int index1=0; index1<weights.length;index1++)
		{
			double sum = 0;
			for(int index2=0; index2 < weights[0].length; index2++)
			{
				//every value of the array is passing through sigmoid function
				sum = sum + weights[index1][index2]*neurons[index2];
			}
			vector[index1] = sum;
		}
		return vector;
	}
	double[] activation(double[] array) 
	{
		for(int index=0; index<array.length;index++)
		{
			array[index] = (1/( 1 + Math.pow(Math.E,(-1*array[index]))));
		}
		return array;
	}
	int getOutput()
	{
		int index = -1;
		double max = -2;
		for(int i=0;i<this.O_layer.length;i++)
		{
			if(this.O_layer[i] > max)
			{
				max = this.O_layer[i];
				index = i;
			}
		}
		return index;
	}
}
