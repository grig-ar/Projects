/*
version 0.3.3
*/

#include"T1_head.h"

using namespace std;

int main(int argc, char* argv[])
{
	try
	{
		stringstream ss;
		vector<string> buf;
		map<string, int> ct;
		vector<pair<string, int>> sct;
		switch (argc)
		{
		case 1:
			cout << "Sample: phrases [-n] (length of phrase) [-m] (num of repeats) [full path to filename | - ]" << '\n'
				<< "phrases -n 3 -m 4 - we all live in a yellow submarine" << '\n'
				<< "phrases c:\\temp\\words.txt\n";
			break;
		case 2: //phrases filename
			if (string(argv[1]) == "-")
				buf = text2phrases(2, cin);
			else
			{
				ifstream ist{ argv[1] };
				if (!ist)
				{
					cout << "Can't open input file " << argv[1] << '\n';
					exit(1);
				}
				buf = text2phrases(2, ist);
			}
			ct = countphr(buf);
			sct = sortphrases(ct, 2);
			outvec(sct);
			break;
		case 4: // phrases -n 3 filename | phrases -m 4 filename
			if ((string(argv[1]) != "-n") && (string(argv[1]) != "-m"))
			{
				cout << "Format error!\n";
				break;
			}
			if ((string(argv[3]) == "-") && (string(argv[1]) == "-n"))
				buf = text2phrases(stoi(argv[2]), cin);
			if ((string(argv[3]) == "-") && (string(argv[1]) != "-n"))
				buf = text2phrases(2, cin);
			if (string(argv[3]) != "-")
			{
				ifstream ist{ argv[3] };
				if (!ist)
				{
					cout << "Can't open input file " << argv[3] << '\n';
					exit(1);
				}
				if (string(argv[1]) == "-n")
					buf = text2phrases(stoi(argv[2]), ist);
				else 
					buf = text2phrases(2, ist);
			}
				ct = countphr(buf);
				if (string(argv[1]) == "-m")
					sct = sortphrases(ct, stoi(argv[2]));
				else
					sct = sortphrases(ct, 2);
				outvec(sct);
				break;
		case 6: // phrases -n 3 -m 4 filename | phrases -m 4 -n 3 filename
			if ((string(argv[1]) != "-n") && (string(argv[3]) != "-m") && (string(argv[3]) != "-n") && (string(argv[1]) != "-m"))
			{
				cout << "Format error!\n";
				break;
			}
			if ((string(argv[5]) == "-") && (string(argv[1]) == "-n"))
				buf = text2phrases(stoi(argv[2]), cin);
			if ((string(argv[5]) == "-") && (string(argv[3]) == "-n"))
				buf = text2phrases(stoi(argv[4]), cin);
			else
			{
				ifstream ist{ argv[5] };
				if (!ist)
				{
					cout << "Can't open input file " << argv[1] << '\n';
					exit(1);
				}
				if (string(argv[1]) == "-n")
					buf = text2phrases(stoi(argv[2]), ist);
				if (string(argv[3]) == "-n")
					buf = text2phrases(stoi(argv[4]), ist);
			}
				ct = countphr(buf);
				if (string(argv[3]) == "-m")
					sct = sortphrases(ct, stoi(argv[4]));
				if (string(argv[1]) == "-m")
					sct = sortphrases(ct, stoi(argv[2]));
				outvec(sct);
			break;
		default:
			cout << "Format error!\n";
			break;
		}
	}
	catch (Bad_phrlen)
	{
		cout << "Invalid arg in text2phrases() function\n";
	}
	catch (Bad_repeats)
	{
		cout << "Invalid arg in sortphr() function\n";
	}
	catch (...)
	{
		cout << "Unknown exception!\n";
	}
	return 0;
}