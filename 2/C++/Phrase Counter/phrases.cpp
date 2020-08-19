/*
version 0.3.2
*/

#include "T1_head.h"

using namespace std;

vector<string> text2phrases(int phrlen, istream& ist)
{
	if (phrlen <= 0) throw Bad_phrlen{};
	vector<string> buffer;
	int i = 0;
	bool first = true;
	unsigned int p;
	string buf1;
	string temp_phr = "";
	if (phrlen == 1)
		i = 1;
	while (ist >> buf1)
	{
		if (phrlen == 1)
		{
			buffer.push_back(buf1);
			continue;
		}
		else
		{
			if (first)
			{
				for (;;)
				{
					temp_phr += buf1 + " ";
					++i;
					break;
				}
				if (i != phrlen)
					continue;
				temp_phr = temp_phr.substr(0, temp_phr.length() - 1);
				buffer.push_back(temp_phr);
				p = temp_phr.find(" ");
				temp_phr = temp_phr.substr(p + 1, temp_phr.length());
				first = false;
				continue;
			}
			temp_phr += " " + buf1;
			buffer.push_back(temp_phr);
			p = temp_phr.find(" ");
			temp_phr = temp_phr.substr(p + 1, temp_phr.length());
		}
	}
	if (i < phrlen)
	{
		cout << "Length of phrase: " << phrlen << " > amount of given words: " << i << '\n';
		exit(1);
	}
	return buffer;
}

map<string, int> countphr(const vector<string>& buffer)
{
	map<string, int> phrases;
	for (unsigned int i = 0; i < buffer.size(); ++i)
		++phrases[buffer[i]];
	return phrases;
}

vector<pair<string, int>> sortphrases(const map<string, int>& phrases, int repeats)
{
	if (repeats <= 0) throw Bad_repeats{};
	vector<pair<string, int>> phr;
	for (const auto& p : phrases)
	{
		if (p.second >= repeats)
			phr.push_back(p);
	}
	if (phr.size() == 0)
	{
		cout << "There are no such phrases.\n";
		exit(1);
	}
	if (repeats != 1)
	{
		for (unsigned int i = 0; i < phr.size() - 1; ++i)
		{
			for (unsigned int j = i + 1; j < phr.size(); ++j)
			{
				if (phr[i].second < phr[j].second)
					swap(phr[i], phr[j]);
			}
		}
	}
	return phr;
}

void outvec(const vector<pair<string, int>>& v)
{
	for (const auto& p : v)
		cout << p.first << " " << '(' << p.second << ')' << '\n';
	return;
}