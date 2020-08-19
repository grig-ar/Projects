#include "T3_head.h"

using namespace std;

vector< vector<int> > arrange(vector< vector<int> > vec)
{
	srand(time(NULL));
	unsigned int s = rand() % 3 + 1;
	switch (s)
	{
	case 1:
		vec = arrangeFirst(vec);
		break;
	case 2:
		vec = arrangeSecond(vec);
		break;
	case 3:
		vec = arrangeThird(vec);
		break;
	default:
		break;
	}
	return vec;
}

vector< vector<int> > arrangeFirst(vector< vector<int> > vec)
{
	srand(time(NULL));
	vector<int> amount_ships = { 0, 4, 3, 2, 1 };
	vector<int> end_f = { 0, 4, 0, 0, 0 };
	unsigned int ship_len = rand() % 3 + 2;
	unsigned int rotation = rand() % 4;
	int k = -1;
	unsigned int x = 0;
	unsigned int y = 0;
		for (unsigned int i = 0; i < ship_len; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
		--amount_ships[ship_len];
		++y;
		if (y == 3)
		{
			vector<int> var = { 2, 4, 3 };
			ship_len = var[rand() % 3];
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
			++y;
			if (y == 6)
			{
				ship_len = 4;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
			if (y == 7)
			{
				ship_len = 3;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
			if (y == 8)
			{
				ship_len = 2;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
		}
		if (y == 5)
		{
			vector<int> var = { 2, 3 };
			ship_len = var[rand() % 2];
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
			++y;
			if (y == 9)
			{
				ship_len = 3;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++x;
				}
				--amount_ships[ship_len];
			}
			else
			{
				ship_len = 2;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
		}
		if (y == 4)
		{
			vector<int> var = { 2, 3 };
			ship_len = var[rand() % 2];
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
			++y;
			if (y == 7)
			{
				ship_len = 3;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
			else
			{
				ship_len = 2;
				for (unsigned int i = 0; i < ship_len; ++i)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
			}
		}
		unsigned int i = 0;
		x = 2;
		y = 0;
		while (amount_ships != end_f)
		{
			while (k < 1)
				k = amount_ships[rand() % 3 + 2];
			for (i = 0; i < amount_ships.size(); ++i)
			{
				if (amount_ships[i] == k)
					break;
			}
			while (k != 0)
			{
				ship_len = i;
				for (unsigned int j = 0; j < ship_len; ++j)
				{
					vec[x][y] = 1;
					++y;
				}
				--amount_ships[ship_len];
				++y;
				--k;
			}
		}
		for (int k = 0; k < 10; ++k)
			vec[3][k] = 8;
		while (amount_ships[1] != 0)
		{
			x = rand() % 6 + 4;
			y = rand() % 10;
			while (vec[x][y] == 8 || vec[x][y] == 1)
			{
				x = rand() % 6 + 4;
				y = rand() % 10;
			}
			vec[x][y] = 1;
			--amount_ships[1];
			for (int i = -1; i < 2; ++i)
			{
				for (int j = -1; j < 2; ++j)
				{
					if (((x + i) < 10) && ((y + j) < 10) && ((x + i) > 2) && ((y + j) >= 0))
					{
						if (vec[x + i][y + j] != 1)
							vec[x + i][y + j] = 8;
					}
				}
			}
		}
		for (int i = 0; i < 10; ++i)
		{
			for (int j = 0; j < 10; ++j)
			{
				if (vec[i][j] == 8)
					vec[i][j] = 0;
			}

		}
	for (unsigned int i = 0; i < rotation; ++i)
		vec = rotate(vec);
	return vec;
}

vector< vector<int> > arrangeSecond(vector< vector<int> > vec)
{
	srand(time(NULL));
	int amount_ships = 4;
	unsigned int rotation = rand() % 4;
	int k = -1;
	unsigned int x = 0;
	unsigned int y = 0;
	//print_field(vec);
	//cout << '\n';
	for (unsigned int i = 0; i < 4; ++i)
	{
		vec[x][y] = 1;
		++x;
	}
	++x;
	//print_field(vec);
	//cout << '\n';
	for (unsigned int i = 0; i < 3; ++i)
	{
		vec[x][y] = 1;
		++x;
	}
	++x;
	//print_field(vec);
	//cout << '\n';
	vec[x][y] = 1;
	vec[x][1] = 1;
	x = 0;
	y = 2;
	//print_field(vec);
	//cout << '\n';
	if ((rand() % 100) % 2 == 0)
	{
		for (unsigned int i = 0; i < 3; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
		++y;
		for (unsigned int i = 0; i < 2; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
	}
	else
	{
		for (unsigned int i = 0; i < 2; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
		++y;
		for (unsigned int i = 0; i < 3; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
	}
	++y;
	//print_field(vec);
	//cout << '\n';
	vec[x][y] = 1;
	vec[1][y] = 1;
	/*print_field(vec);
	cout << '\n';*/
	//print_field(vec);
	//cout << '\n';
	unsigned int i = 0;
	x = 0;
	y = 2;
	for (int k = 1; k < 9; ++k)
	{
		vec[1][k] = 8;
		vec[k][1] = 8;
	}
	vec[8][2] = 8;
	vec[9][2] = 8;
	vec[2][8] = 8;
	vec[2][9] = 8;
	//print_field(vec);
	//cout << '\n';
	while (amount_ships != 0)
	{
		x = rand() % 8 + 2;
		y = rand() % 8 + 2;
		while (vec[x][y] == 8 || vec[x][y] == 1)
		{
			x = rand() % 8 + 2;
			y = rand() % 8 + 2;
		}
		vec[x][y] = 1;
		--amount_ships;
		for (int i = -1; i < 2; ++i)
		{
			for (int j = -1; j < 2; ++j)
			{
				if (((x + i) < 10) && ((y + j) < 10) && ((x + i) > 2) && ((y + j) >= 0))
				{
					if (vec[x + i][y + j] != 1)
						vec[x + i][y + j] = 8;
				}
			}
		}
		//print_field(vec);
		//cout << '\n';
	}
	//print_field(vec);
	//cout << '\n';
	for (int i = 0; i < 10; ++i)
	{
		for (int j = 0; j < 10; ++j)
		{
			if (vec[i][j] == 8)
				vec[i][j] = 0;
		}

	}
	//print_field(vec);
	//cout << '\n';
	for (unsigned int i = 0; i < rotation; ++i)
		vec = rotate(vec);
	return vec;
}

vector< vector<int> > arrangeThird(vector< vector<int> > vec)
{
	srand(time(NULL));
	vector<int> amount_ships = { 0, 4, 3, 2, 1 };
	vector<int> end_f = { 0, 4, 0, 0, 0 };
	unsigned int ship_len = rand() % 3 + 2;
	unsigned int rotation = rand() % 4;
	int k = -1;
	unsigned int x = 0;
	unsigned int y = 0;
	for (unsigned int i = 0; i < ship_len; ++i)
	{
		vec[x][y] = 1;
		++y;
	}
	--amount_ships[ship_len];
	++y;
	if (y == 3)
	{
		vector<int> var = { 2, 4, 3 };
		ship_len = var[rand() % 3];
		for (unsigned int i = 0; i < ship_len; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
		--amount_ships[ship_len];
		++y;
		if (y == 6)
		{
			ship_len = 4;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
		}
		if (y == 7)
		{
			ship_len = 3;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
		}
		if (y == 8)
		{
			ship_len = 2;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
		}
	}
	if (y == 5)
	{
		for (unsigned q = 0; q < 2; ++q)
		{
			ship_len = 2;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
			++y;
		}
	}
	if (y == 4)
	{
		vector<int> var = { 2, 3 };
		ship_len = var[rand() % 2];
		for (unsigned int i = 0; i < ship_len; ++i)
		{
			vec[x][y] = 1;
			++y;
		}
		--amount_ships[ship_len];
		++y;
		if (y == 7)
		{
			ship_len = 3;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
		}
		else
		{
			ship_len = 2;
			for (unsigned int i = 0; i < ship_len; ++i)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
		}
	}
	unsigned int i = 0;
	x = 9;
	y = 0;
	while (amount_ships != end_f)
	{
		while (k < 1)
			k = amount_ships[rand() % 3 + 2];
		for (i = 0; i < amount_ships.size(); ++i)
		{
			if (amount_ships[i] == k)
				break;
		}
		while (k != 0)
		{
			ship_len = i;
			for (unsigned int j = 0; j < ship_len; ++j)
			{
				vec[x][y] = 1;
				++y;
			}
			--amount_ships[ship_len];
			++y;
			--k;
		}
	}
	for (int k = 0; k < 10; ++k)
	{
		vec[2][k] = 8;
		vec[8][k] = 8;
	}
	while (amount_ships[1] != 0)
	{
		x = rand() % 6 + 2;
		y = rand() % 10;
		while (vec[x][y] == 8 || vec[x][y] == 1)
		{
			x = rand() % 6 + 2;
			y = rand() % 10;
		}
		vec[x][y] = 1;
		--amount_ships[1];
		for (int i = -1; i < 2; ++i)
		{
			for (int j = -1; j < 2; ++j)
			{
				if (((x + i) < 10) && ((y + j) < 10) && ((x + i) > 2) && ((y + j) >= 0))
				{
					if (vec[x + i][y + j] != 1)
						vec[x + i][y + j] = 8;
				}
			}
		}
	}
	for (int i = 0; i < 10; ++i)
	{
		for (int j = 0; j < 10; ++j)
		{
			if (vec[i][j] == 8)
				vec[i][j] = 0;
		}

	}
	for (unsigned int i = 0; i < rotation; ++i)
		vec = rotate(vec);
	return vec;
}

vector< vector<int> > rotate(vector< vector<int> >& vec)
{
	int tmp;
	for (int i = 0; i < 5; ++i)
	{
		for (int j = i; j < 10 - 1 - i; ++j)
		{
			tmp = vec[i][j];
			vec[i][j] = vec[10 - j - 1][i];
			vec[10 - j - 1][i] = vec[10 - i - 1][10 - j - 1];
			vec[10 - i - 1][10 - j - 1] = vec[j][10 - i - 1];
			vec[j][10 - i - 1] = tmp;
		}
	}
	return vec;
}

vector<pair<int, int>> rotateVector(vector<pair<int, int>>& vec)
{
	pair<int, int> temp;
	for (unsigned int i = 0; i < vec.size(); ++i)
	{
		temp = vec[i];
		vec[i].first = 10 - temp.second - 1;
		vec[i].second = temp.first;
	}
	return vec;
}

string findBattleship(vector< vector<int> > vec, string msg)
{
	//string msg = "";
	string str;
	string temp = "";
	char y_t;
	bool flag;
	stringstream ss;
	vector <pair<int, int>> pos = { {3,0},{7,0},{9,2},{9,6} };
	for (int i = 0; i < 2; ++i)
	{
		for (int j = pos[i].first; j > -1; --j)
		{
			ss.str("");
			ss.clear();
			flag = CheckMessage(msg);
			//cin >> msg;
			if (flag/*msg == "Shoot!"*/)
			{
				while (vec[pos[i].first][pos[i].second] != 0 && j > 0)
				{
					--pos[i].first;
					++pos[i].second;
					--j;
				}
					str = "";
					y_t = pos[i].second + 'A';
					ss << y_t << " " << to_string(pos[i].first);
					getline(ss, str);
					cout << str << '\n';
					--pos[i].first;
					++pos[i].second;
					msg = "";
					cin >> msg;
					if (msg == "Kill")
					{
						vec[pos[i].first][pos[i].second] = 1;
						for (int p = -1; p < 2; ++p)
						{
							for (int q = -1; q < 2; ++q)
								if ((pos[i].first + p) < 10 && (pos[i].first + p) > -1 && (pos[i].second + q) < 10 && (pos[i].second + q) > -1 && vec[pos[i].first][pos[i].second] != 1)
									vec[pos[i].first][pos[i].second] = 8;

						}
						continue;
					}
					if (msg == "Hit")
					{
						temp = killingShip(vec, pos[i].first, pos[i].second);
						if (temp == "Killed 4 length ship")
						{
							msg = "Kill";
							return msg;
						}
					}
					else
					{
						msg = "Miss";
						continue;

					}
				}
		}
	}
	for (int i = 2; i < 4; ++i)
	{
		for (int j = pos[i].second; j < 10; ++j)
		{
			ss.str("");
			ss.clear();
			flag = CheckMessage(msg);
			//cin >> msg;
			if (flag/*msg == "Shoot!"*/)
			{
				while (vec[pos[i].first][pos[i].second] != 0 && j > 0)
				{
					--pos[i].first;
					++pos[i].second;
					--j;
				}
					str = "";
					y_t = pos[i].second + 'A';
					ss << y_t << " " << to_string(pos[i].first);
					getline(ss, str);
					cout << str << '\n';
					--pos[i].first;
					++pos[i].second;
					msg = "";
					cin >> msg;
					if (msg == "Kill")
					{
						vec[pos[i].first][pos[i].second] = 1;
						for (int p = -1; p < 2; ++p)
						{
							for (int q = -1; q < 2; ++q)
								if ((pos[i].first + p) < 10 && (pos[i].first + p) > -1 && (pos[i].second + q) < 10 && (pos[i].second + q) > -1 && vec[pos[i].first][pos[i].second] != 1)
									vec[pos[i].first][pos[i].second] = 8;

						}
						continue;
					}
					if (msg == "Hit")
					{
						temp = killingShip(vec, pos[i].first, pos[i].second);
						if (temp == "Killed 4 length ship")
						{
							msg = "Kill";
							return msg;
						}
					}
					else
					{
						msg = "Miss";
						continue;
					}
				}
		}
	}
	return msg;
}

string findCruiser_Destroyer(vector< vector<int> > vec, string msg)
{
	//string msg = "";
	string str;
	bool flag;
	string temp = "";
	char y_t;
	stringstream ss;
	vector <pair<int, int>> pos = { { 1,0 },{ 5,0 },{ 9,0 },{ 9,4 },{ 9,8 } };
	for (int i = 0; i < 3; ++i)
	{
		for (int j = pos[i].first; j > -1; --j)
		{
			ss.str(""); 
			ss.clear();

			//cin >> msg;
			while (vec[pos[i].first][pos[i].second] != 0 && j > 0)
			{
				flag = CheckMessage(msg);
				if (flag/*msg == "Shoot!"*/)
				{
					str = "";
					y_t = pos[i].second + 'A';
					ss << y_t << " " << to_string(pos[i].first);
					getline(ss, str);
					cout << str << '\n';
					msg = "";
					cin >> msg;
					if (msg == "Hit")
						temp = killingShip(vec, pos[i].first, pos[i].second);
					if (msg == "Kill")
					{
						vec[pos[i].first][pos[i].second] = 1;
						for (int p = -1; p < 2; ++p)
						{
							for (int q = -1; q < 2; ++q)
								if ((pos[i].first + p) < 10 && (pos[i].first + p) > -1 && (pos[i].second + q) < 10 && (pos[i].second + q) > -1 && vec[pos[i].first][pos[i].second] != 1)
									vec[pos[i].first][pos[i].second] = 8;
						}
						continue;
					}
					else
					{
						--pos[i].first;
						++pos[i].second;
						--j;
						msg = "Miss";
						continue;
					}
				}
				--pos[i].first;
				++pos[i].second;
				--j;
			}

		}
	}
	for (int i = 3; i < 5; ++i)
	{
		for (int j = pos[i].second; j < 10; ++j)
		{
			ss.str(""); 
			ss.clear();
			//cin >> msg;
			while (vec[pos[i].first][pos[i].second] != 0 && j > 0)
			{
				flag = CheckMessage(msg);
				if (flag/*msg == "Shoot!"*/)
				{
					str = "";
					y_t = pos[i].second + 'A';
					ss << y_t << " " << to_string(pos[i].first);
					getline(ss, str);
					cout << str << '\n';
					--pos[i].first;
					++pos[i].second;
					msg = "";
					cin >> msg;
					if (msg == "Hit")
						temp = killingShip(vec, pos[i].first, pos[i].second);
					if (msg == "Kill")
					{
						vec[pos[i].first][pos[i].second] = 1;
						for (int p = -1; p < 2; ++p)
						{
							for (int q = -1; q < 2; ++q)
								if ((pos[i].first + p) < 10 && (pos[i].first + p) > -1 && (pos[i].second + q) < 10 && (pos[i].second + q) > -1 && vec[pos[i].first][pos[i].second] != 1)
									vec[pos[i].first][pos[i].second] = 8;
						}
						continue;
					}
					else
					{
						--pos[i].first;
						++pos[i].second;
						--j;
						msg = "Miss";
						continue;
					}
				}
				--pos[i].first;
				++pos[i].second;
				--j;
			}

		}
	}
	return msg;
}

void shootingRandom(vector< vector<int> > vec, string temp)
{
	string msg = "";
//	string temp = "none";
	char y_t;
	stringstream ss;
	int x,y;
	srand(time(NULL));
	while (msg != "Lose" || msg != "Win!")
	{
	//	print_field(vec);
	//	cout << endl;
		x = rand() % 10;
		y = rand() % 10;
		while (vec[x][y] != 0)
		{
			x = rand() % 10;
			y = rand() % 10;
		}
		if (temp == "none" || temp == "Miss")
			cin >> msg;
		else
			msg = "Shoot!";
		if (msg == "Shoot!")
		{
			temp = "none";
			y_t = y + 'A';
			ss << y_t << " " << to_string(x);
			string str;
			msg = "";
			getline(ss, str);
			cout << str << '\n';
			cin >> msg;
			ss.str("");
			ss.clear();
			if (msg == "Miss")
			{
				vec[x][y] = 8;
				ss.str("");
				ss.clear();
			}
			if (msg == "Hit")
				temp = killingShip(vec, x, y);
			if (msg == "Kill")
			{
				temp = "Killed 1 length ship";
				vec[x][y] = 1;
					//print_field(vec);
					//cout << endl;
				for (int i = -1; i < 2; ++i)
				{
					for (int j = -1; j < 2; ++j)
						if ((x + i) < 10 && (x + i) > -1 && (y + j) < 10 && (y + j) > -1 && vec[x + i][y + j] == 0)
							vec[x + i][y + j] = 8;
				}
			}
		}
		//print_field(vec);
		//cout << endl;
	}
}

string killingShip(vector< vector<int> >& vec, int x, int y)
{
	//ofstream out; 
	//out.open("outputA.txt", ios::app);
	srand(time(NULL));
	stringstream ss;
	string s;
	bool correct;
	bool isEmpty = false;
	bool isInside = false;
	int min_x, max_x, min_y, max_y;
	char y_t;
	vec[x][y] = 1;
	markEdges(vec, x, y);
	vector<pair<int, int>> ship = { {0,0}, {0,0}, {0,0}, {0,0} };
	ship[0].first = x;
	ship[0].second = y;
	int hit = 1;
	int ad_x, ad_y;
	string msg = "Hit";
	//out << msg << '\n';
		while (hit == 1)
		{
			correct = CheckMessage(msg);
			msg = "";
			ss.str("");
			ss.clear();
			string str;
			str = "";
			vector<int> ch = { -1, 0, 1 };

			for (;;)
			{
				ad_x = ch[rand() % 3];
				ad_y = ch[rand() % 3];
				if (ad_x * ad_y != 0)
					continue;
				isInside = false;
				isEmpty = false;
				isInside = ((x + ad_x) < 10) && ((y + ad_y) < 10) && ((x + ad_x) > -1) && ((y + ad_y) > -1);
				if (isInside == true)
					isEmpty = (vec[x + ad_x][y + ad_y] != 8) && (vec[x + ad_x][y + ad_y] != 1);
				if (isEmpty)
					break;
			}
			//print_field(vec);
			y_t = y + ad_y + 'A';
			ss << y_t << " " << to_string(x + ad_x);
			getline(ss, str);
			cout << str << '\n';
		//	out << str << '\n';
			cin >> msg;
			//out << msg << '\n';
			if (msg == "Miss")
			{
				vec[x + ad_x][y + ad_y] = 8;
			//	cout << endl;
				//print_field(vec);
				ss.str(""); 
				ss.clear();
			}
			if (msg == "Hit")
			{
				++hit;
				x += ad_x;
				y += ad_y;
				////print_field(vec);
			//	cout << endl;
				vec[x][y] = 1;
			//	//print_field(vec);
			//	cout << endl;
				markEdges(vec, x, y);
				////print_field(vec);
			//	cout << endl;
				ship[1].first = x;
				ship[1].second = y;
				ss.str(""); 
				ss.clear();
			}
			if (msg == "Kill")
			{
				ss.str(""); 
				ss.clear();
				++hit;
				x += ad_x;
				y += ad_y;
				vec[x][y] = 1;
				markEdges(vec, x, y);
				ship[1].first = x;
				ship[1].second = y;
				min_x = min(ship[0].first, ship[1].first);
				max_x = max(ship[0].first, ship[1].first);
				min_y = min(ship[0].second, ship[1].second);
				max_y = max(ship[0].second, ship[1].second);
				markEnd(vec, x, y, min_x, max_x, min_y, max_y);
				ss << "Killed " << hit << " length ship";
				getline(ss, s);
				//out.close();
				return s;
			}
		}
		msg = "";
		min_x = min(ship[0].first, ship[1].first);
		max_x = max(ship[0].first, ship[1].first);
		min_y = min(ship[0].second, ship[1].second);
		max_y = max(ship[0].second, ship[1].second);
		while (hit == 2)
		{
			string str;
			if (max_x == min_x)
			{
				if (min_y - 1 > -1 && vec[x][min_y-1] != 8)
					y = min_y - 1;
				else
					y = max_y + 1;
				//print_field(vec);
				y_t = y + 'A';
				ss << y_t << " " << to_string(x);
				getline(ss, str);
				cout << str << '\n';
				//out << str << '\n';
				cin >> msg;
				//out << msg << '\n';
				if (msg == "Miss")
				{
					vec[x][y] = 8;
					str = "";
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
					if (min_y - 1 > -1)
						y = max_y + 1;
					else
						y = min_y - 1;
					ss.str(""); 
					ss.clear();
					y_t = y + 'A';
					ss << y_t << " " << to_string(x);
					getline(ss, str);
					cout << str << '\n';
					//out << str << '\n';
					msg = "";
					cin >> msg;
					//out << msg << '\n';
				}
				if (msg == "Hit")
				{
					++hit;
					vec[x][y] = 1;
					markEdges(vec, x, y);
					ship[2].first = x;
					ship[2].second = y;
					ss.str(""); 
					ss.clear();
				}
				if (msg == "Kill")
				{
					//print_field(vec);
					ss.str(""); 
					ss.clear();
					++hit;
			//		cout << '\n';
					vec[x][y] = 1;
					//print_field(vec);
					markEdges(vec, x, y);
					ship[2].first = x;
					ship[2].second = y;
					min_x = min(min_x, ship[2].first);
					max_x = max(max_x, ship[2].first);
					min_y = min(min_y, ship[2].second);
					max_y = max(max_y, ship[2].second);
					markEnd(vec, x, y, min_x, max_x, min_y, max_y);
					ss << "Killed " << hit << " length ship";
					getline(ss, s);
					//out.close();
					return s;
				}
			}
			if (max_y == min_y)
			{
				if (min_x - 1 > -1 && vec[min_x - 1][y] != 8)
					x = min_x - 1;
				else
					x = max_x + 1;
				ss.str(""); 
				ss.clear();
				//print_field(vec);
				y_t = y + 'A';
				ss << y_t << " " << to_string(x);
				string str;
				getline(ss, str);
				cout << str << '\n';
				//out << str << '\n';
				cin >> msg;
				//out << msg << '\n';
				if (msg == "Miss")
				{
					vec[x][y] = 8;
					str = "";
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
					ss.str(""); 
					ss.clear();
					if (min_x - 1 > -1)
						x = max_x + 1;
					else
						x = min_x - 1;
					y_t = y + 'A';
					ss << y_t << " " << to_string(x);
					getline(ss, str);
					cout << str << '\n';
					//out << str << '\n';
					msg = "";
					cin >> msg;
					//out << msg << '\n';
				}
				if (msg == "Hit")
				{
					++hit;
					vec[x][y] = 1;
					markEdges(vec, x, y);
					ship[2].first = x;
					ship[2].second = y;
					ss.str(""); 
					ss.clear();
				}
				if (msg == "Kill")
				{
					ss.str(""); 
					ss.clear();
					++hit;
					vec[x][y] = 1;
					markEdges(vec, x, y);
					ship[2].first = x;
					ship[2].second = y;
					min_x = min(min_x, ship[2].first);
					max_x = max(max_x, ship[2].first);
					min_y = min(min_y, ship[2].second);
					max_y = max(max_y, ship[2].second);
					markEnd(vec, x, y, min_x, max_x, min_y, max_y);
					ss << "Killed " << hit << " length ship";
					getline(ss, s);
					//out.close();
					return s;
				}
			}
		}
		msg = "";
		min_x = min(min_x, ship[2].first);
		max_x = max(max_x, ship[2].first);
		min_y = min(min_y, ship[2].second);
		max_y = max(max_y, ship[2].second);
		while (hit == 3)
		{
			string str;
			if (max_x == min_x)
			{
				if (min_y - 1 > -1 && vec[x][min_y - 1] != 8)
					y = min_y - 1;
				else
					y = max_y + 1;
				//print_field(vec);
				y_t = y + 'A';
				ss << y_t << " " << to_string(x);
				getline(ss, str);
				cout << str << '\n';
				//out << str << '\n';
				cin >> msg;
				//out << msg << '\n';
				if (msg == "Miss")
				{
					vec[x][y] = 8;
					str = "";
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
					ss.str(""); 
					ss.clear();
					if (min_y - 1 > -1)
						y = max_y + 1;
					else
						y = min_y - 1;
					y_t = y + 'A';
					ss << y_t << " " << to_string(x);
					getline(ss, str);
					cout << str << '\n';
					//out << str << '\n';
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
				}
				if (msg == "Hit")
				{
					++hit;
					vec[x][y] = 1;
					markEdges(vec, x, y);
					ship[3].first = x;
					ship[3].second = y;
					ss.str(""); 
					ss.clear();
				}
				if (msg == "Kill")
				{
					ss.str(""); 
					ss.clear();
					++hit;
					//print_field(vec);
				//	cout << '\n';
					vec[x][y] = 1;
					//print_field(vec);
					markEdges(vec, x, y);
					ship[3].first = x;
					ship[3].second = y;
					min_x = min(min_x, ship[3].first);
					max_x = max(max_x, ship[3].first);
					min_y = min(min_y, ship[3].second);
					max_y = max(max_y, ship[3].second);
					markEnd(vec, x, y, min_x, max_x, min_y, max_y);
					ss << "Killed " << hit << " length ship";
					getline(ss, s);
					//out.close();
					return s;
				}
			}
			if (max_y == min_y)
			{
				if (min_x - 1 > -1 && vec[min_x - 1][y] != 8)
					x = min_x - 1;
				else
					x = max_x + 1;
				ss.str(""); 
				ss.clear();
				//print_field(vec);
				y_t = y + 'A';
				ss << y_t << " " << to_string(x);
				string str;
				getline(ss, str);
				cout << str << '\n';
				//out << str << '\n';
				cin >> msg;
				//out << msg << '\n';
				if (msg == "Miss")
				{
					vec[x][y] = 8;
					str = "";
					ss.str(""); 
					ss.clear();
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
					if (min_x - 1 > -1)
						x = max_x + 1;
					else
						x = min_x - 1;
					y_t = y + 'A';
					ss << y_t << " " << to_string(x);
					getline(ss, str);
					cout << str << '\n';
					//out << str << '\n';
					msg = ""; //n
					cin >> msg;
					//out << msg << '\n';
				}
				if (msg == "Hit")
				{
					++hit;
					vec[x][y] = 1;
					markEdges(vec, x, y);
					ship[3].first = x;
					ship[3].second = y;
					ss.str(""); 
					ss.clear();
				}
				if (msg == "Kill")
				{
					ss.str(""); 
					ss.clear();
					++hit;
					//print_field(vec);
				//	cout << '\n';
					vec[x][y] = 1;
					//print_field(vec);
					markEdges(vec, x, y);
					ship[3].first = x;
					ship[3].second = y;
					min_x = min(min_x, ship[3].first);
					max_x = max(max_x, ship[3].first);
					min_y = min(min_y, ship[3].second);
					max_y = max(max_y, ship[3].second);
					markEnd(vec, x, y, min_x, max_x, min_y, max_y);
					ss << "Killed " << hit << " length ship";
					getline(ss, s);
					//out.close();
					return s;
				}
			}
		}


	ss.str(""); 
	ss.clear();
	ss << "Killed " << hit << " length ship";
	getline(ss, s);
	//out.close();
	return s;
}

void markEdges(vector< vector<int> >& vec, int x, int y)
{
	for (int i = -1; i < 2; ++i)
	{
		for (int j = -1; j < 2; ++j)
		{
			if ((i * j) != 0 && (x + i) < 10 && (x + i) > -1 && (y + j) < 10 && (y + j) > -1)
				vec[x + i][y + j] = 8;
		}
	}
}

void markEnd(vector< vector<int> >& vec, int x, int y, int min_x, int max_x, int min_y, int max_y)
{
	if (min_x == max_x)
	{
		if (min_y - 1 > -1 && vec[x][min_y - 1] == 0)
			vec[x][min_y - 1] = 8;
		if (max_y + 1 < 10 && vec[x][max_y + 1] == 0)
			vec[x][max_y + 1] = 8;
	}
	if (min_y == max_y)
	{
		if (min_x - 1 > -1 && vec[min_x - 1][y] == 0)
			vec[min_x - 1][y] = 8;
		if (max_x + 1 < 10 && vec[max_x + 1][y] == 0)
			vec[max_x + 1][y] = 8;
	}
}

vector<bool> checkCorners(vector< vector<int> >& vec)
{
	string msg = "";
	string temp = "none";
	char y_t;
	stringstream ss;
	string s = "";
	vector<pair<int, int>> corners = { {0,0}, {9,0}, {9,9}, {0,9} };
	vector<bool> flags = { false, false, false, false, false };
	for (int i = 0; i < 4; ++i)
	{
		if (temp == "none")
			cin >> msg;
		else
			msg = "Shoot!";
		if (msg == "Shoot!")
		{
			temp = "none";
			y_t = corners[i].first + 'A';
			ss << y_t << " " << to_string(corners[i].second);
			string str;
			msg = "";
			getline(ss, str);
			cout << str << '\n';
			cin >> msg;
			ss.str("");
			ss.clear();
			if (msg == "Miss")
				vec[corners[i].second][corners[i].first] = 8;
			if (msg == "Hit")
			{
				temp = killingShip(vec, corners[i].second, corners[i].first);
				flags[i] = true;
			}
			if (msg == "Kill")
			{
				temp = "Killed 1 length ship";
				vec[corners[i].second][corners[i].first] = 1;
				for (int p = -1; p < 2; ++p)
				{
					for (int j = -1; j < 2; ++j)
						if ((corners[i].second + p) < 10 && (corners[i].second + p) > -1 && (corners[i].first + j) < 10 && (corners[i].first + j) > -1 && vec[corners[i].second + p][corners[i].first + j] == 0)
							vec[corners[i].second + p][corners[i].first + j] = 8;
				}
			}
		}
	}
	if (msg == "Shoot!" || msg == "Kill" || msg == "Hit")
		flags[4] = true;
	return flags;
}

vector<bool> checkFirstPattern(vector< vector<int> >& vec, int rotate_amount, string temp)
{
	/*for (int i = 0; i < rotate_amount; ++i)
		vec = rotate(vec);*/
	string msg = "";
	//string temp = "A9 is ALWAYS DEAD";
	char y_t;
	stringstream ss;
	string s = "";
	vector<pair<int, int>> points = { { 0,5 },{ 2,1 },{ 2,3 },{ 2,6 },{ 2,8 } };
	for (int i = 0; i < rotate_amount; ++i)
		points = rotateVector(points);
	int amount = 0;
	vector<bool> flag = { false, false };
	for (int i = 0; i < 5; ++i)
	{
		if (vec[points[i].second][points[i].first] == 0)
		{
			if (temp == "none")
				cin >> msg;
			else
				msg = "Shoot!";
			if (msg == "Shoot!")
			{
				temp = "none";
				y_t = points[i].first + 'A';
				ss << y_t << " " << to_string(points[i].second);
				string str;
				msg = "";
				getline(ss, str);
				cout << str << '\n';
				cin >> msg;
				ss.str("");
				ss.clear();
				if (msg == "Miss")
					vec[points[i].second][points[i].first] = 8;
				if (msg == "Hit")
				{
					temp = killingShip(vec, points[i].second, points[i].first);
					++amount;
				}
			}
			if (msg == "Kill")
			{
				temp = "Killed 1 length ship";
				vec[points[i].second][points[i].first] = 1;
				for (int p = -1; p < 2; ++p)
				{
					for (int j = -1; j < 2; ++j)
						if ((points[i].second + p) < 10 && (points[i].second + p) > -1 && (points[i].first + j) < 10 && (points[i].first + j) > -1 && vec[points[i].second + p][points[i].first + j] == 0)
							vec[points[i].second + p][points[i].first + j] = 8;
				}
			}
		}
		else
			if (vec[points[i].second][points[i].first] == 1)
				++amount;
	}
	if (amount >= 4)
		flag[0] = true;
	if (msg == "Shoot!" || msg == "Kill" || msg == "Hit")
		flag[1] = true;
	return flag;
}

vector<bool> checkSecondPattern(vector< vector<int> >& vec, int rotate_amount, string temp)
{
	//for (int i = 0; i < rotate_amount; ++i)
	//	vec = rotate(vec);
	string msg = "";
	//string temp = "A9 is ALWAYS DEAD";
	char y_t;
	stringstream ss;
	string s = "";
	vector<pair<int, int>> points = { { 2,0 },{ 6,0 },{ 0,7 }, { 0,3 } };
	for (int i = 0; i < rotate_amount; ++i)
		points = rotateVector(points);
	int amount = 0;
	vector<bool> flag = { false, false };
	for (int i = 0; i < 4; ++i)
	{
		if (vec[points[i].second][points[i].first] == 0)
		{
			if (temp == "none")
				cin >> msg;
			else
				msg = "Shoot!";
			if (msg == "Shoot!")
			{
				temp = "none";
				y_t = points[i].first + 'A';
				ss << y_t << " " << to_string(points[i].second);
				string str;
				msg = "";
				getline(ss, str);
				cout << str << '\n';
				cin >> msg;
				ss.str("");
				ss.clear();
				if (msg == "Miss")
					vec[points[i].second][points[i].first] = 8;
				if (msg == "Hit")
				{
					temp = killingShip(vec, points[i].second, points[i].first);
					++amount;
				}
				if (msg == "Kill")
				{
					temp = "Killed 1 length ship";
					vec[points[i].second][points[i].first] = 1;
					for (int p = -1; p < 2; ++p)
					{
						for (int j = -1; j < 2; ++j)
							if ((points[i].second + p) < 10 && (points[i].second + p) > -1 && (points[i].first + j) < 10 && (points[i].first + j) > -1 && vec[points[i].second + p][points[i].first + j] == 0)
								vec[points[i].second + p][points[i].first + j] = 8;
					}
				}
			}
		}
		else
			if (vec[points[i].second][points[i].first] == 1)
				++amount;
	}
	if (amount >= 3)
		flag[0] = true;
	if (msg == "Shoot!" || msg == "Kill" || msg == "Hit")
		flag[1] = true;
	return flag;
}

vector<bool> checkThirdPattern(std::vector< std::vector<int> >& vec, int rotate_amount, string temp)
{
	//for (int i = 0; i < rotate_amount; ++i)
	//	vec = rotate(vec);
	string msg = "";
	//string temp = "A9 is ALWAYS DEAD";
	char y_t;
	stringstream ss;
	string s = "";
	vector<pair<int, int>> points = { { 0,5 },{ 9,5 }, { 5,0 }, { 4,9 } };
	//for (int i = 0; i < rotate_amount; ++i)
	//	points = rotateVector(points);
	int amount = 0;
	vector<bool> flag = { false, false };
	for (int i = 0; i < 4; ++i)
	{
		if (vec[points[i].second][points[i].first] == 0)
		{
			if (temp == "none")
				cin >> msg;
			else
				msg = "Shoot!";
			if (msg == "Shoot!")
			{
				temp = "none";
				y_t = points[i].first + 'A';
				ss << y_t << " " << to_string(points[i].second);
				string str;
				msg = "";
				getline(ss, str);
				cout << str << '\n';
				cin >> msg;
				ss.str("");
				ss.clear();
				if (msg == "Miss")
					vec[points[i].second][points[i].first] = 8;
				if (msg == "Hit")
				{
					temp = killingShip(vec, points[i].second, points[i].first);
					++amount;
				}
				if (msg == "Kill")
				{
					temp = "Killed 1 length ship";
					vec[points[i].second][points[i].first] = 1;
					for (int p = -1; p < 2; ++p)
					{
						for (int j = -1; j < 2; ++j)
							if ((points[i].second + p) < 10 && (points[i].second + p) > -1 && (points[i].first + j) < 10 && (points[i].first + j) > -1 && vec[points[i].second + p][points[i].first + j] == 0)
								vec[points[i].second + p][points[i].first + j] = 8;
					}
				}
			}
		}
		else
			if (vec[points[i].second][points[i].first] == 1)
				++amount;
	}
	if (amount >= 2)
		flag[0] = true;
	if (msg == "Shoot!" || msg == "Kill" || msg == "Hit")
		flag[1] = true;
	return flag;
}

vector<bool> checkPatterns(vector< vector<int> >& enemy_f, vector<bool> corn)
{
	vector<bool> temp = { false, false };
	bool fl = false;
	if (corn[0] == true && corn[1] == false && corn[2] == false && corn[3] == true && corn[4] == false)
	{
		temp = checkFirstPattern(enemy_f, 0, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == false && corn[3] == false && corn[4] == false)
	{
		temp = checkFirstPattern(enemy_f, 1, "none");
		fl = true;
	}
	if (corn[0] == false && corn[1] == true && corn[2] == true && corn[3] == false && corn[4] == false)
	{
		temp = checkFirstPattern(enemy_f, 2, "none");
		fl = true;
	}
	if (corn[0] == false && corn[1] == false && corn[2] == true && corn[3] == true && corn[4] == false)
	{
		temp = checkFirstPattern(enemy_f, 3, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == false && corn[3] == true && corn[4] == false)
	{
		temp = checkSecondPattern(enemy_f, 0, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == true && corn[3] == false && corn[4] == false)
	{
		temp = checkSecondPattern(enemy_f, 1, "none");
		fl = true;
	}
	if (corn[0] == false && corn[1] == true && corn[2] == true && corn[3] == true && corn[4] == false)
	{
		temp = checkSecondPattern(enemy_f, 2, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == false && corn[2] == true && corn[3] == true && corn[4] == false)
	{
		temp = checkSecondPattern(enemy_f, 3, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == true && corn[3] == true && corn[4] == false)
	{
		temp = checkThirdPattern(enemy_f, 0, "none");
		fl = true;
	}
	if (corn[0] == true && corn[1] == false && corn[2] == false && corn[3] == true && corn[4] == true)
	{
		temp = checkFirstPattern(enemy_f, 0, "Shoot!");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == false && corn[3] == false && corn[4] == true)
	{
		temp = checkFirstPattern(enemy_f, 1, "Shoot!");
		fl = true;
	}
	if (corn[0] == false && corn[1] == true && corn[2] == true && corn[3] == false && corn[4] == true)
	{
		temp = checkFirstPattern(enemy_f, 2, "Shoot!");
		fl = true;
	}
	if (corn[0] == false && corn[1] == false && corn[2] == true && corn[3] == true && corn[4] == true)
	{
		temp = checkFirstPattern(enemy_f, 3, "Shoot!");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == false && corn[3] == true && corn[4] == true)
	{
		temp = checkSecondPattern(enemy_f, 0, "Shoot!");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == true && corn[3] == false && corn[4] == true)
	{
		temp = checkSecondPattern(enemy_f, 1, "Shoot!");
		fl = true;
	}
	if (corn[0] == false && corn[1] == true && corn[2] == true && corn[3] == true && corn[4] == true)
	{
		temp = checkSecondPattern(enemy_f, 2, "Shoot!");
		fl = true;
	}
	if (corn[0] == true && corn[1] == false && corn[2] == true && corn[3] == true && corn[4] == true)
	{
		temp = checkSecondPattern(enemy_f, 3, "Shoot!");
		fl = true;
	}
	if (corn[0] == true && corn[1] == true && corn[2] == true && corn[3] == true && corn[4] == true)
	{
		temp = checkThirdPattern(enemy_f, 0, "Shoot!");
		fl = true;
	}
	if (!fl)
		temp[1] = corn[4];
	return temp;
}

void print_field(vector< vector<int> > vec)
{
	for (int i = 0; i < 10; ++i)
	{
		
		for (int j = 0; j < 10; ++j)
			cout << vec[i][j];
		cout << '\n';
	}
}

bool CheckMessage(string message)
{
	string tempor = message;
	message = "";
	while (tempor == "Miss" || tempor == "")
	{
		cin >> message;
		tempor = message;
		message = "";
	}
	return true;
}
