#include <iostream>
#include <iomanip>
#include <math.h>
#include <vector>
using namespace std;

double Val(double a, double b, double c, double x) {
	return pow(x, 3.0) + a * pow(x, 2.0) + b * x + c;
}

double derVal(double a, double b, double x) {
	return 3 * pow(x, 2.0) + a * 2 * x + b;
}

double findRoot(double a, double b, double c, double x1, double x2) {
	double E = 0.000001;
	double x = (x1 + x2) / 2;
	while (abs(Val(a, b, c, x)) > E) {
		x = (x1 + x2) / 2;
		if (Val(a, b, c, x) > 0) {
			x2 = x;
		}
		if (Val(a, b, c, x) < 0) {
			x1 = x;
		}
	}
	return x;
}

int main() {
	double A = 12.0;
	double B = 6.0;
	double C = 1.0;
	double E = 0.000001;
	double val;
	double x = 0;
	double min, max;
	double temp1, temp2;
	double Discr;
	vector<double> roots;
	bool end = false;
	if (C == 0) {
		roots.push_back(0.0);
		Discr = A * A - 4 * B;
		if (Discr = 0) {
			roots.push_back((-A + sqrt(Discr)) / 2);
			end = true;
		}
		if (Discr > 0) {
			roots.push_back((-A + sqrt(Discr)) / 2);
			roots.push_back((-A - sqrt(Discr)) / 2);
			end = true;
		}
	}
	if (!end) {
		Discr = 4 * A * A - 12 * B;
		if (Discr <= 0) {
			if (C > 0) {
				while (Val(A, B, C, x) > 0)
					--x;
				roots.push_back(findRoot(A, B, C, x, x + 1));
			}
			if (C < 0) {
				while (Val(A, B, C, x) < 0)
					++x;
				roots.push_back(findRoot(A, B, C, x, x + 1));
			}
		}
		if (Discr > 0) {
			temp1 = (-2 * A + sqrt(Discr)) / 6;
			temp2 = (-2 * A - sqrt(Discr)) / 6;
			if ((derVal(A, B, temp1 + E) > 0) && (derVal(A, B, temp1 - E) < 0)) {
				min = temp1;
				max = temp2;
			}
			else {
				min = temp2;
				max = temp1;
			}
			if (Val(A, B, C, max) > 0 && Val(A, B, C, min) > 0) {
				x = max;
				while (Val(A, B, C, x) > 0)
					--x;
				roots.push_back(findRoot(A, B, C, x, x + 1));
			}
			if (Val(A, B, C, max) > E && Val(A, B, C, min) < -E) {
				x = max;
				while (Val(A, B, C, x) > 0)
					--x;
				roots.push_back(findRoot(A, B, C, x, x + 1));
				x = min;
				while (Val(A, B, C, x) < 0)
					++x;
				roots.push_back(findRoot(A, B, C, x - 1, x));
				roots.push_back(findRoot(A, B, C, min, max));
			}
			if (Val(A, B, C, max) < 0 && Val(A, B, C, min) < 0) {
				x = min;
				while (Val(A, B, C, x) < 0)
					++x;
				roots.push_back(findRoot(A, B, C, x - 1, x));
			}
			if (abs(Val(A, B, C, min)) < E) {
				x = max;
				while (Val(A, B, C, x) > 0)
					--x;
				roots.push_back(findRoot(A, B, C, x, x + 1));
				roots.push_back(min);
				roots.push_back(min);
			}
			if (abs(Val(A, B, C, max)) < E) {
				x = min;
				while (Val(A, B, C, x) < 0)
					++x;
				roots.push_back(findRoot(A, B, C, x - 1, x));
				roots.push_back(max);
				roots.push_back(max);
			}
		}
	}
	for (double n : roots) {
		cout << fixed << setprecision(6) << n << '\n';
	}
	return 0;
}