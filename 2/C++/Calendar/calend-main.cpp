#include "T2_head.h"

using namespace std;

int main()
{
	try
	{
		string str, str2, str3, str4;
		Calendar ccc(99, Month::feb, 28, 25, 30, 0);
		str = ccc.toString();
		Calendar ccc2(2012, Month::jun, 27, 23, 59, 59);
		++ccc2;
		str2 = ccc2.toString();
		DateInterval interv = get_interval(ccc2, ccc);
		str3 = interv.toString();
		ccc = add_interval(ccc, interv);
		str4 = ccc.toString();
		cout << str << '\n' << str2 << '\n' << str3 << '\n' << str4 << '\n';
		//str = ccc.formatDate("ss@YYYY@MMM@MM@DD");
		//DateInterval int1(ccc2, ccc);
		//ccc = ccc.add_second(-1);
		//str = ccc.toString();
		//str2 = ccc2.toString();
		//cout << str  << '\n' << str2 << '\n';
		//str3 = int1.toString();
		//ccc = ccc + int1;
		//str2 = ccc.toString();
		//cout << str3 << '\n' << str2 << '\n';
		//cout << str << '\n';
	}
	catch (Calendar::Invalid)
	{
		cout << "Invalid date!\n";
	}
	return 0;
}