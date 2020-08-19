#pragma once

#include<iostream>
#include<iomanip>
#include<fstream>
#include<sstream>
#include<cmath>
#include<cstdlib>
#include<string>
#include<vector>
#include <regex>
#include<stdexcept>
#include<map>
#include <ctime>

extern std::vector<char> store;

class Turn_Limit{ };
class Low_Limit { };
class Bad_File { };

class Field
{
public:
	Field();
	void addCell(int x, int y);
	void clearCell(int x, int y);
	int aliveEnv(int x, int y);
//	int deadEnv(int x, int y);
	bool surv(char t, int a);
	void step();
	void back();
	void reset();
	void saveToFile(std::string name);
	void loadFromFile(std::string name);
	void incTurn();
	void decTurn();
	std::vector<char>& retField() { return my_f; }
	void printField();
private:
	std::vector<char> my_f;
	static int counter;
};




