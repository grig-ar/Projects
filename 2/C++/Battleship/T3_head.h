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


std::vector< std::vector<int> > arrange(std::vector< std::vector<int> > vec);
std::vector< std::vector<int> > arrangeFirst(std::vector< std::vector<int> > vec);
std::vector< std::vector<int> > arrangeSecond(std::vector< std::vector<int> > vec);
std::vector< std::vector<int> > arrangeThird(std::vector< std::vector<int> > vec);
std::vector< std::vector<int> > rotate(std::vector< std::vector<int> >& vec);
std::vector<std::pair<int, int>> rotateVector(std::vector<std::pair<int, int>>& vec);
void shootingRandom(std::vector< std::vector<int> > vec, std::string meesage);
std::string findBattleship(std::vector< std::vector<int> > vec, std::string message);
std::string findCruiser_Destroyer(std::vector< std::vector<int> > vec, std::string message);
void markEdges(std::vector< std::vector<int> >& vec, int x, int y);
void markEnd(std::vector< std::vector<int> >& vec, int x, int y, int min_x, int max_x, int min_y, int max_y);
std::vector<bool> checkCorners(std::vector< std::vector<int> >& vec);
bool CheckMessage(std::string message);
std::vector<bool> checkPatterns(std::vector< std::vector<int> >& vec, std::vector<bool> corn);
std::vector<bool> checkFirstPattern(std::vector< std::vector<int> >& vec, int rotate_amount, std::string temp);
std::vector<bool> checkSecondPattern(std::vector< std::vector<int> >& vec, int rotate_amount, std::string temp);
std::vector<bool> checkThirdPattern(std::vector< std::vector<int> >& vec, int rotate_amount, std::string temp);
std::string killingShip(std::vector< std::vector<int> >& vec, int x, int y);
void print_field(std::vector< std::vector<int> > vec);