// Lab3_thread_pool.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include "pch.h"
#include <iostream>
#include <thread>
#include <time.h>


using namespace std;

int** createMatrix(int height, int width)
{
	int** matrix = 0;
	matrix = new int*[height];
	for (int h = 0; h < height; h++)
	{
		matrix[h] = new int[width];
		for (int w = 0; w < width; w++)
		{
			matrix[h][w] = w + h;
		}
	}

	return matrix;
}

void oneCell(int i, int j, int m, int** a, int** b, int** product) {
	int sum = 0;
	for (int k = 0; k < m; k++) {
		int c = a[i][k];
		int d = b[k][j];
		int e = a[i][k] * b[k][j];
		sum += e;
		//sum += a[i][k] * b[k][j];
	}
	// cout << "result of " << i << " " << j << " " << sum << endl;
	product[i][j] = sum;
}

void computeMatrixCells(int startI, int endI, int startJ, int endJ, int m, int width, int**a, int**b, int**product) {
	//cout << "Compute " << startI << " - " << endI << " " << startJ << " - " << endJ << endl;
	int i = startI, j;

	// if there's one row or less to add 
	if (startI == endI) {
		for (j = startJ; j <= endJ; j++) {
			// cout << i << " " << j << endl;
			oneCell(i, j, m, a, b, product);
		}
		return;
	}

	// first row, starting from startJ up to the end
	for (j = startJ; j < width; j++) {
		// cout << startI << " " << j << endl;
		oneCell(startI, j, m, a, b, product);
	}
	// the rest of the rows, except the last one
	while (i < endI)
	{
		i++;
		for (j = 0; j < width; j++) {
			// cout << i << " " << j << endl;
			oneCell(i, j, m, a, b, product);
		}
	}
	// the last row, from the beginning up to endJ
	for (j = 0; j <= endJ; j++) {
		// cout << endI << " " << j << endl;
		oneCell(endI, j, m, a, b, product);
	}
}

void displayMatrix(int r, int c, int** matrix) {
	int i, j;
	for (i = 0; i < r; ++i)
		for (j = 0; j < c; ++j)
		{
			cout << matrix[i][j] << "  ";
			if (j == c - 1)
				cout << endl;
		}
}

int main()
{
	cout << "First Matrix " << endl;
	int height1, width1;
	cout << "Number of rows (between 1 and 100): ";
	cin >> height1;
	cout << "Number of columns (between 1 and 100): ";
	cin >> width1;
	cout << "Second Matrix " << endl;

	int height2 = width1, width2;
	cout << "Number of rows will be " << height2 << endl;
	cout << "Number of columns (between 1 and 100): ";
	cin >> width2;

	int** a = createMatrix(height1, width1);
	int** b = createMatrix(height2, width2);
	int m = width1;
	// the product matrix
	int height3 = height1, width3 = width2;

	int** sum = new int*[height1];
	for (int i = 0; i < height1; i++) {
		sum[i] = new int[width2];
	}

	int n;
	//cout << "Enter the number of threads: " << endl;
	//cin >> n;
	//if (n > width3*height3) n = width3 * height3;

	for (n = 1; n <= height3 * width3; n++) {

		const clock_t begin_time = clock();

		thread* threads = new thread[n];
		int startI = 0, endI = 0, startJ = 0, endJ = 0;
		for (int i = 0; i < n - 1; i++)
		{
			endJ = height3 * width3 / n - 1;
			while (endJ >= width3) {
				endI++;
				endJ -= width3;
			}
			threads[i] = thread(computeMatrixCells, startI, endI, startJ, endJ, m, width3, a, b, sum);

			// update startI and startJ for the next thread
			startI = endI;
			startJ = endJ + 1;
			if (startJ >= width3) {
				startI++;
				startJ -= width3;
			}
		}
		endI = height3 - 1;
		endJ = width3 - 1;
		threads[n - 1] = thread(computeMatrixCells, startI, endI, startJ, endJ, m, width3, a, b, sum);

		for (int i = 0; i < n; i++)
		{
			threads[i].join();
		}

		std::cout << n << " threads - " << float(clock() - begin_time) / CLOCKS_PER_SEC << endl;
	}

	// Displaying the resultant sum matrix.
	cout << endl << "Product of the two matrices is: " << endl;
	displayMatrix(height1, width1, a);
	cout << endl;
	displayMatrix(height2, width2, b);
	cout << endl;
	displayMatrix(height3, width3, sum);

	return 0;
}