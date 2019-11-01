// Lab2.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include "pch.h"
#include <iostream>
#include <thread>
#include <future>
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

void addMatrices(int height, int width, int** a, int**b, int** sum) {
	int i,j;
		for (i = 0; i < height; ++i) {
			sum[i] = new int[width];
			for (j = 0; j < width; ++j)
				sum[i][j] = a[i][j] + b[i][j];
	}
		
}

void addMatrixCells(int startI, int endI, int startJ, int endJ, int width, int**a, int**b, int**sum) {
	int i = startI, j;

	// if there's one row or less to add 
	if (startI == endI) {
		for (j = startJ; j <= endJ; j++)
			sum[i][j] = a[i][j] + b[i][j];
		return;
	}

	// first row, starting frm startJ up to the end
	for (j = startJ; j < width; j++)
		sum[i][j] = a[i][j] + b[i][j];
	// the rest of the rows, except the last one
	while (i<endI)
	{
		for (j = 0; j< width; j++)
			sum[i][j] = a[i][j] + b[i][j];
		i++;
	}
	// the last row, from the beginning up to endJ
	for (j = 0; j <= endJ; j++) {
		sum[i][j] = a[i][j] + b[i][j];
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
	int height, width;
	cout << "Number of rows (between 1 and 100): ";
	cin >> height;
	cout << "Number of columns (between 1 and 100): ";
	cin >> width;
	int** a = createMatrix(height, width);
	int** b = createMatrix(height, width);
	int** sum = new int*[height];
	for (int i = 0; i < height; i++) {
		sum[i] = new int[width];
	}

	int n;
	for (n = 1; n <= height * width; n+=2) {

		const clock_t begin_time = clock();

		thread* threads = new thread[n];
		int startI = 0, endI = 0, startJ = 0, endJ = 0;
		for (int i = 0; i < n - 1; i++)
		{
			endJ += height * width / n - 1;
			while (endJ >= width) {
				endI++;
				endJ -= width;
			}
			// cout << "Thread " << i << " adds " << startI << " - " << endI << ", " << startJ << " - " << endJ << endl;
			threads[i] = thread(addMatrixCells, startI, endI, startJ, endJ, width, a, b, sum);
			// update startI and startJ for the next thread
			startI = endI;
			startJ = endJ + 1;
			if (startJ >= width) {
				startI++;
				startJ -= width;
			}
		}
		endI = height - 1;
		endJ = width - 1;
		// cout << "Thread " << n-1 << " adds " << startI << " - " << endI << ", " << startJ << " - " << endJ << endl;
		threads[n - 1] = thread(addMatrixCells, startI, endI, startJ, endJ, width, a, b, sum);

		for (int i = 0; i < n; i++)
		{
			threads[i].join();
		}

		std::cout << n << " threads - " << float(clock() - begin_time) / CLOCKS_PER_SEC << endl;

		
	}

	// Displaying the resultant sum matrix.
		//cout << endl << "Sum of the two matrices is: " << endl;
	displayMatrix(height, width, sum);


	return 0;
}