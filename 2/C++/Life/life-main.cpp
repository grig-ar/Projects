#include "T4_head.h"

using namespace std;

int main()
{
	try {
		Field f;
		f.printField();
		string msg = "";
		while (msg.compare(0, 4, "Exit") != 0)
		{
			getline(cin, msg);
			if (msg.compare(0, 3, "set") == 0)
			{
				if (int(msg[4]) > 74 || int(msg[4]) < 65)
				{
					cout << "Wrong coordinat!\n";
					f.printField();
					msg = "";
					continue;
				}
				f.addCell(int(msg[4]) - 65, int(msg[5]) - 48);
				f.printField();
				msg = "";
				continue;
			}
			if (msg.compare(0, 5, "clear") == 0)
			{
				if (int(msg[4]) > 74 || int(msg[4]) < 65)
				{
					cout << "Wrong coordinat!\n";
					f.printField();
					msg = "";
					continue;
				}
				f.clearCell(int(msg[6]) - 65, int(msg[7]) - 48);
				f.printField();
				msg = "";
				continue;
			}
			if (msg.compare(0, 4, "step") == 0 && msg.length() == 4)
			{
				f.step();
				f.incTurn();
				f.printField();
				msg = "";
				continue;
			}
			if (msg.compare(0, 4, "step") == 0 && msg.length() != 4)
			{
				if (int(msg[5]) > 57 || int(msg[5]) < 48)
				{
					cout << "Wrong step amount!\n";
					f.printField();
					msg = "";
					continue;
				}
				string t = msg.substr(5, msg.length() - 4);
				int amount = stoi(t);
				for (int i = 0; i < amount; ++i)
				{
					f.step();
					f.incTurn();
					f.printField();
				}
				msg = "";
				continue;
			}
			if (msg.compare(0, 4, "back") == 0)
			{
				f.back();
				f.decTurn();
				f.printField();
				msg = "";
				continue;
			}
			if (msg.compare(0, 5, "reset") == 0)
			{
				f.reset();
				f.printField();
				msg = "";
				continue;
			}
			if (msg.compare(0, 4, "save") == 0)
			{
				string name = msg.substr(5, msg.length() - 4);
				f.saveToFile(name);
				msg = "";
				continue;
			}
			if (msg.compare(0, 4, "load") == 0)
			{
				string name = msg.substr(5, msg.length() - 4);
				f.loadFromFile(name);
				f.printField();
				msg = "";
				continue;
			}
			else
			{
				if (msg.compare(0, 4, "Exit") != 0)
				{
					cout << "Unknown command!\n";
					continue;
				}
			}
		}
	}
	catch (Turn_Limit)
	{
		cout << "100 turns limit!\n";
	}
	catch (Low_Limit)
	{
		cout << "Error: back from turn 0!\n";
	}
	catch (Bad_File)
	{
		cout << "Error: incorrect file!\n";
	}
	catch (...)
	{
		cout << "Unknown exception!\n";
	}
	return 0;
}