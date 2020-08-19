#include "T4_head.h"

using namespace std;
int Field::counter = 0;
vector<char> store(10000);

Field::Field()
{
	while (my_f.size() != 100)
		my_f.push_back('.');
}

void Field::addCell(int x, int y)
{
	my_f[y * 10 + x] = '*';
	return;
}

void Field::clearCell(int x, int y)
{
	my_f[y * 10 + x] = '.';
	return;
}

void Field::reset()
{
	for (int i = 0; i < 10; ++i)
		for (int j = 0; j < 10; ++j)
			clearCell(i, j);
	counter = 0;
}

int Field::aliveEnv(int x, int y)
{
	int al = 0;
	int x_t;
	int y_t;
	for (int i = -1; i < 2; ++i)
	{
		for (int j = -1; j < 2; ++j)
		{
			x_t = x;
			y_t = y;
			if (i == 0 && j == 0)
				continue;
			if (x + i < 0)
				x_t += 10;
			if (y + j < 0)
				y_t += 10;
			if (my_f[((x_t + i) % 10) * 10 + (y_t + j) % 10] == '*')
				++al;
		}
	}
	return al;
}

//int Field::deadEnv(int x, int y)
//{
//	int de = 0;
//	int x_t;
//	int y_t;
//	for (int i = -1; i < 2; ++i)
//	{
//		for (int j = -1; j < 2; ++j)
//		{
//			x_t = x;
//			y_t = y;
//			if (i == 0 && j == 0)
//				continue;
//			if (x + i < 0)
//				x_t += 10;
//			if (y + j < 0)
//				y_t += 10;
//			if (my_f[((x_t + i) % 10) * 10 + (y_t + j) % 10] == '.')
//				++de;
//		}
//	}
//	return de;
//}

bool Field::surv(char t, int a) // survival options
{
	if (t == '.' && a == 3)
		return true;
	if (t == '*' && (a == 3 || a == 2))
		return true;
	return false;
}

void Field::step()
{
	if (counter == 100)
		throw Turn_Limit{};
	for (int i = counter*100; i < (counter+1)*100; ++i)
		store[i] = my_f[i % 100];
	int a = 0;
	vector<char> temp(100);
	for (int i = 0; i < 10; ++i)
	{
		for (int j = 0; j < 10; ++j)
		{
			a = Field::aliveEnv(i, j);
			if (Field::surv(my_f[i * 10 + j], a))
				temp[i * 10 + j] = '*';
			else
				temp[i * 10 + j] = '.';
		}
	}
	my_f = temp;
	return;
}

void Field::back()
{
	if (counter == 0)
		throw Low_Limit{};
	for (int i = (counter-1) * 100; i < (counter) * 100; ++i)
		my_f[i % 100] = store[i];
	return;
}

void Field::incTurn()
{
	counter++;
	return;
}

void Field::decTurn()
{
	counter--;
	return;
}

void Field::printField()
{
	cout << "Turn: " << counter << '\n';
	cout << "  ";
	for (int i = 0; i < 10; ++i)
	{
		char cord = 'A' + i;
		cout << cord << " ";
	}
	cout << '\n';
	for (int i = 0; i < 10; ++i)
	{
		cout << i << " ";
		for (int j = 0; j < 10; ++j)
		{
			cout << my_f[i * 10 + j] << " ";
		}
		cout << '\n';
	}
	return;
}

void Field::saveToFile(string name)
{
	ofstream out;
	out.open(name);
	if (!out.is_open())
	{
		cout << "File can't be opened!\n";
		return;
	}
	for (int i = 0; i < 10; ++i)
	{
		for (int j = 0; j < 10; ++j)
		{
			out << my_f[i * 10 + j] << " ";
		}
		out << '\n';
	}
	cout << "Successfully saved to file!\n";
	out.close();
	return;
}

void Field::loadFromFile(string name)
{
	ifstream in;
	in.open(name);
	if (!in.is_open())
	{
		cout << "File can't be opened!\n";
		return;
	}
	string buffer;
	int k = 0;
	while (getline(in, buffer))
	{	
		for (int i = 0; i < 9; ++i)
		{
			if (buffer[2 * i] != '.' && buffer[2 * i] != '*')
				throw Bad_File{};
			my_f[k] = buffer[2 * i];
			++k;
		}
		++k;
	}
	counter = 0;
	in.close();
	return;
}