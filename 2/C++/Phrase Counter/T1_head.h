#pragma once

/*
version 0.3.2
*/

#include<iostream>
#include<fstream>
#include<sstream>
#include<cstdlib>
#include<string>
#include<regex>
#include<vector>
#include<map>

class Bad_repeats { };
class Bad_phrlen { };

std::vector<std::string> text2phrases(int phrlen, std::istream& ist);
std::map<std::string, int> countphr(const std::vector<std::string>& buffer);
std::vector<std::pair<std::string, int>> sortphrases(const std::map<std::string, int>& phrases, int repeats);
void outvec(const std::vector<std::pair<std::string, int>>& v);