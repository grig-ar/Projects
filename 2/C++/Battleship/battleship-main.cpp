#include "T3_head.h"

using namespace std;

int main()
{
	string message = "";
	cin >> message;
	vector<bool> temp = { false, false };
	bool correctMsg = false;
	vector<vector<int> > my_f(10, vector<int>(10, 0));
	vector<vector<int> > enemy_f(10, vector<int>(10, 0));
	vector<bool> corn;
	while (message != "Win!" || message != "Lose")
	{
		string ss = "";
		if (message == "Arrange!")
		{
			my_f = arrange(my_f);
			print_field(my_f);
		}
		corn = checkCorners(enemy_f);
		temp = checkPatterns(enemy_f, corn);
		if (temp[0] == true && temp[1] == true)
				shootingRandom(enemy_f, "Killed");
		if (temp[0] == true && temp[1] == false)
			shootingRandom(enemy_f, "none");
		if (temp[0] == false && temp[1] == true)
		{
			ss = "Killed";
			//ss = findCruiser_Destroyer(enemy_f, ss);
			shootingRandom(enemy_f, ss);
		}
		if (temp[0] == false && temp[1] == false)
		{
			ss = "Miss";
			//ss = findCruiser_Destroyer(enemy_f, ss);
			shootingRandom(enemy_f, ss);
		}

	}
	return 0;
}