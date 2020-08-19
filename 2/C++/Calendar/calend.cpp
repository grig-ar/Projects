#include "T2_head.h"

/*
version 0.3.1 (09.11.17)
*/

using namespace std;

void Calendar::normalize(unsigned int yy, Month mm, unsigned int dd, unsigned int hh, unsigned int mmin, unsigned int ss)
{
	const vector<unsigned int> d_in_m = { 29, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	s = ss % 60;
	int minAdd = ss / 60;
	mmin += minAdd;
	min = mmin % 60;
	int hAdd = mmin / 60;
	hh += hAdd;
	h = hh % 24;
	int dAdd = hh / 24;
	dd += dAdd;
	int mAdd = 0;
	int yAdd = 0;
	if (leapyear(yy) && mm == Month::feb)
	{
		int mAdd = dd / (d_in_m[0] + 1);
		yAdd = (int(mm) + mAdd) / 13;
		if ((dd % d_in_m[0] == 0) && mAdd == 0)
			d = d_in_m[0];
		else
			d = dd % d_in_m[0];
		if (yAdd)
			m = Month((int(mm) + mAdd) % 12);
		else
			m = Month((int(mm) + mAdd));
	}
	else
	{
		int mAdd = dd / (d_in_m[int(mm)] + 1);
		yAdd = (int(mm) + mAdd) / 13;
		if ((dd % d_in_m[int(mm)] == 0) && mAdd == 0)
			d = d_in_m[int(mm)];
		else
			d = dd % d_in_m[int(mm)];
		if (yAdd)
			m = Month((int(mm) + mAdd) % 12);
		else
			m = Month((int(mm) + mAdd));
	}
	yy += yAdd;
	y = yy;
	return;
}

Calendar::Calendar(unsigned int yy, Month mm, unsigned int dd, unsigned int hh, unsigned int mmin, unsigned int ss)
{
	normalize(yy, mm, dd, hh, mmin, ss);
	if (!is_date(y, m, d, h, min, s)) throw Invalid{};
}

Calendar::Calendar(unsigned int yy, Month mm, unsigned int dd)
{
	normalize(yy, mm, dd, 0, 0, 0);
	if (!is_date(y, m, d, h, min, s)) throw Invalid{};
}

Calendar::Calendar(unsigned int hh, unsigned int mmin, unsigned int ss)
{
	time_t t = time(nullptr);
	struct tm aTm;
	gmtime_r(&aTm, &t);
	normalize(aTm.tm_year + 1900, Month(aTm.tm_mon + 1), aTm.tm_mday, hh, mmin, ss);
	if (!is_date(y, m, d, h, min, s)) throw Invalid{};
}

Calendar::Calendar()
{
	time_t t = time(nullptr);
	struct tm aTm;
	gmtime_s(&aTm, &t);
	y = aTm.tm_year + 1900;
	m = Month(aTm.tm_mon + 1);
	d = aTm.tm_mday;
	h = aTm.tm_hour;
	min = aTm.tm_min;
	s = aTm.tm_sec;
}

Calendar::Calendar(const Calendar &c)
{
	y = c.y;
	m = c.m;
	d = c.d;
	h = c.h;
	min = c.min;
	s = c.s;
}

Calendar Calendar::add_second(int n) const
{
	Calendar cNew(*this);
	if (int(n + s) >= 0)
	{
		cNew = cNew.add_minute((n + s) / 60);
		cNew.s = (n + cNew.s) % 60;
	}
	else //n + s < 0
	{
		cNew = cNew.add_minute((int(n + cNew.s) - 60) / 60);
		cNew.s = 60 + (int(n + cNew.s) % 60);
	}
	return cNew;
}

Calendar Calendar::add_minute(int n) const
{
	Calendar cNew(*this);
	if (int(n + cNew.min) >= 0)
	{
		cNew = cNew.add_hour((n + cNew.min) / 60);
		cNew.min = (n + cNew.min) % 60;
	}
	else //n + min < 0
	{
		cNew = cNew.add_hour((int(n + cNew.min) - 60) / 60);
		cNew.min = 60 + (int(n + cNew.min) % 60);
	}
	return cNew;
}

Calendar Calendar::add_hour(int n) const
{
	Calendar cNew(*this);
	if (int(n + cNew.h) >= 0)
	{
		cNew = cNew.add_day((n + cNew.h) / 24);
		cNew.h = (n + cNew.h) % 24;
	}
	else //n + h < 0
	{
		cNew = cNew.add_day((int(n + cNew.h) - 24) / 24);
		cNew.h = 24 + (int(n + cNew.h) % 24);
	}
	return cNew;
}

Calendar Calendar::add_day(int n) const
{
	Calendar cNew(*this);
	const vector<unsigned int> d_in_m = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	if (n > 0)
	{
		while (n != 0)
		{
			if (cNew.d < d_in_m[int(cNew.m)] || (cNew.m == Month::feb && leapyear(cNew.y) == true && cNew.d != 29))
			{
				--n;
				++cNew.d;
			}
			else
			{
				--n;
				cNew.d = 1;
				if (cNew.m != Month::dec)
					cNew.m = Month(int(cNew.m) + 1);
				else
				{
					++cNew.y;
					cNew.m = Month::jan;
				}
			}
		}
	}
	else // (n <= 0)
	{
		while (n != 0)
		{
			if (cNew.d > 1)
			{
				++n;
				--cNew.d;
			}
			else
			{
				++n;
				if (cNew.m == Month::mar && leapyear(cNew.y))
				{
					cNew.d = 29;
					cNew.m = Month(int(cNew.m) - 1);
					continue;
				}
				if (cNew.m != Month::jan)
				{
					cNew.d = d_in_m[int(cNew.m) - 1];
					cNew.m = Month(int(cNew.m) - 1);
				}
				else
				{
					--cNew.y;
					cNew.m = Month::dec;
					cNew.d = 31;
				}
			}
		}
	}
	return cNew;
}

Calendar Calendar::add_month(int n) const
{
	Calendar cNew(*this);
	const vector<unsigned int> d_in_m = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	if (n + int(cNew.m) > 0)
	{
		cNew = cNew.add_year((n + int(cNew.m)) / 12);
		if (d_in_m[(int(cNew.m) + n) % 12] < d)
			cNew.d = d_in_m[(int(cNew.m) + n) % 12];
		cNew.m = Month(int((int(cNew.m) + n) % 12));
	}
	else //(n + int(m) <= 0)
	{
		cNew = cNew.add_year((n + int(cNew.m) - 12) / 12);
		if (d_in_m[12 + ((int(cNew.m) + n) % 12)] < d)
			cNew.d = d_in_m[12 + ((int(cNew.m) + n) % 12)];
		cNew.m = Month(int(12 + ((int(cNew.m) + n) % 12)));
	}
	return cNew;
}

Calendar Calendar::add_year(int n) const
{
	Calendar cNew(*this);
	if (!is_date(cNew.y + n, cNew.m, cNew.d, cNew.h, cNew.min, cNew.s)) throw Invalid{};
	if (n > 0 && cNew.m == Month::feb && cNew.d == 29 && !leapyear(cNew.y + n) )
	{
		cNew.m = Month::mar;
		cNew.d = 1;
	}
	if (n < 0 && cNew.m == Month::mar && cNew.d == 1 && leapyear(cNew.y + n))
	{
		cNew.m = Month::feb;
		cNew.d = 29;
	}
	cNew.y += n;
	return cNew;
}

bool is_date(int y, Month m, int d, int h, int min, int s)
{
	if (y < 1 || y > 9999) return false;
	if (d <= 0) return false;
	if (m < Month::jan || Month::dec < m) return false;
	int days_in_month = 31;
	switch (m)
	{
	case Month::feb:
		days_in_month = (leapyear(y)) ? 29 : 28;
		break;
	case Month::apr: case Month::jun: case Month::sep: case Month::nov:
		days_in_month = 30;
		break;
	default:
	}
	if (days_in_month < d) return false;
	if (h < 0 || h > 23) return false;
	if (min < 0 || min > 59) return false;
	if (s < 0 || s > 59) return false;
	return true;
}

bool leapyear(unsigned int y)
{
	if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) return true;
	return false;
}

bool operator== (const Calendar& a, const Calendar& b)
{
	return a.year() == b.year()
		&& a.month() == b.month()
		&& a.day() == b.day()
		&& a.hour() == b.hour()
		&& a.minute() == b.minute()
		&& a.second() == b.second();
}

bool operator!= (const Calendar& a, const Calendar& b)
{
	return !(a == b);
}

string Calendar::toString() const 
{
	const vector<string> ms = { "Null","jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	stringstream ss;
	string str;
	ss.fill('0');
	ss.width(4);
	ss << y;
	ss.width(1);
	ss << '-';
	ss.width(3);
	ss << ms[int(m)];
	ss.width(1);
	ss << '-';
	ss.width(2);
	ss << d;
	ss.width(1);
	ss << ' ';
	ss.width(2);
	ss << h;
	ss.width(1);
	ss << ':';
	ss.width(2);
	ss << min;
	ss.width(1);
	ss << ':';
	ss.width(2);
	ss << s;
	getline(ss, str);
	return str;
}

void parse(int n, vector<int>& vec) 
{

	vec.push_back(n % 10);
	int temp = n / 10;
	if (temp == 0) return;
	parse(temp, vec);

}

string itos(int a)
{
	string str;
	vector<int> number;
	parse(a, number);
	for (unsigned int i = number.size(); i > 0; --i)
		str += (number[i-1] + '0');
	return str;
}

string Calendar::formatDate(string format)
{
	bool invalid = true;
	const vector<string> ms = { "Null","jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	size_t found = string::npos;
	found = format.find("YYYY");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (y < 10)
			to = "000" + itos(y);
		if (y < 100 && y >= 10)
			to = "00" + itos(y);
		if (y < 1000 && y >= 100)
			to = "0" + itos(y);
		if (y >= 1000)
			to = itos(y);
		format.replace(found, 4, to);
		found += 4;
		found = format.find("YYYY");
	}
	found = string::npos;
	found = format.find("MMM");
	while (found != string::npos)
	{
		invalid = false;
		format.replace(found, 3, ms[int(m)]);
		found += 3;
		found = format.find("MMM");
	}
	found = string::npos;
	found = format.find("MM");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (int(m) < 10)
			to = "0" + itos(int(m));
		if (int(m) >= 10)
			to = itos(int(m));
		format.replace(found, 2, to);
		found += 2;
		found = format.find("MM");
	}
	found = string::npos;
	found = format.find("DD");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (d < 10)
			to = "0" + itos(d);
		if (d >= 10)
			to = itos(d);
		format.replace(found, 2, to);
		found += 2;
		found = format.find("DD");
	}
	found = string::npos;
	found = format.find("hh");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (h < 10)
			to = "0" + itos(h);
		if (h >= 10)
			to = itos(h);
		format.replace(found, 2, to);
		found += 2;
		found = format.find("hh");
	}
	found = string::npos;
	found = format.find("mm");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (min < 10)
			to = "0" + itos(min);
		if (min >= 10)
			to = itos(min);
		format.replace(found, 2, to);
		found += 2;
		found = format.find("mm");
	}
	found = string::npos;
	found = format.find("ss");
	while (found != string::npos)
	{
		invalid = false;
		string to;
		if (s < 10)
			to = "0" + itos(s);
		if (s >= 10)
			to = itos(s);
		format.replace(found, 2, to);
		found += 2;
		found = format.find("ss");
	}
	if (invalid)
		format = "Invalid date format";
	return format;
}

Calendar operator+ (const Calendar &c, const DateInterval &interv)
{
	Calendar cCopy(c);
	cCopy = cCopy.add_year(interv.di_year());
	cCopy = cCopy.add_month(interv.di_month());
	cCopy = cCopy.add_day(interv.di_day());
	cCopy = cCopy.add_hour(interv.di_hour());
	cCopy = cCopy.add_minute(interv.di_minute());
	cCopy = cCopy.add_second(interv.di_second());
	return cCopy;
}

Calendar& operator+= (Calendar &c, const DateInterval &interv)
{
	c = c.add_year(interv.di_year());
	c = c.add_month(interv.di_month());
	c = c.add_day(interv.di_day());
	c = c.add_hour(interv.di_hour());
	c = c.add_minute(interv.di_minute());
	c = c.add_second(interv.di_second());
	return c;
}

Calendar& Calendar::operator++()
{
	*this = this->add_day(1);
	return *this;
}

Calendar Calendar::operator++(int)
{
	Calendar c(*this);
	*this = this->add_day(1);
	return c;
}

Calendar operator- (Calendar &c, const DateInterval &interv) 
{
	c = c.add_year(-interv.di_year());
	c = c.add_month(-interv.di_month());
	c = c.add_day(-interv.di_day());
	c = c.add_hour(-interv.di_hour());
	c = c.add_minute(-interv.di_minute());
	c = c.add_second(-interv.di_second());
	return c;
}

Calendar& operator-= (Calendar &c, const DateInterval &interv)
{
	c = c.add_year(-interv.di_year());
	c = c.add_month(-interv.di_month());
	c = c.add_day(-interv.di_day());
	c = c.add_hour(-interv.di_hour());
	c = c.add_minute(-interv.di_minute());
	c = c.add_second(-interv.di_second());
	return c;
}

Calendar& Calendar::operator--()
{
	*this = this->add_day(-1);
	return *this;
}

Calendar Calendar::operator--(int)
{
	Calendar c(*this);
	*this = this->add_day(-1);
	return c;
}

DateInterval get_interval(Calendar& first, Calendar& another)
{
	DateInterval d(first, another);
	return d;
}
Calendar add_interval(const Calendar& c, const DateInterval& interv)
{
	Calendar cCopy(c);
	cCopy = (c + interv);
	return cCopy;
}

string DateInterval::toString() const
{
	stringstream ss;
	string str, s;
	if (yr == 0 && mh == 0 && dy == 0 && hr == 0 && me == 0 && sd == 0)
	{
		ss << "Equal dates \n";
		getline(ss, str);
		s += str;
	}
	else
	{
		ss << "Difference between dates: \n";
		getline(ss, str);
		s += str;
		if (yr != 0)
		{
			if (yr == 1)
			{
				ss << yr << " Year \n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << yr << " Years \n";
				getline(ss, str);
				s += str;
			}
		}
		if (mh != 0)
		{
			if (mh == 1)
			{
				ss << mh << " Month \n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << mh << " Months \n";
				getline(ss, str);
				s += str;
			}
		}
		if (dy != 0)
		{
			if (dy == 1)
			{
				ss << dy << " Day \n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << dy << " Days \n";
				getline(ss, str);
				s += str;
			}
		}
		if (hr != 0)
		{
			if (hr == 1)
			{
				ss << hr << " Hour \n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << hr << " Hours \n";
				getline(ss, str);
				s += str;
			}
		}
		if (me != 0)
		{
			if (me == 1)
			{
				ss << me << " Minute \n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << me << " Minutes \n";
				getline(ss, str);
				s += str;
			}
		}
		if (sd != 0)
		{
			if (sd == 1)
			{
				ss << sd << " Second\n";
				getline(ss, str);
				s += str;
			}
			else
			{
				ss << sd << " Seconds\n";
				getline(ss, str);
				s += str;
			}
		}
	}
	return s;
}

bool operator> (Calendar& v1, Calendar& v2)
{
	if (v1.year() != v2.year())
		return v1.year() > v2.year();
	if (int(v1.month()) != int(v2.month()))
		return int(v1.month()) > int(v2.month());
	if (v1.day() != v2.day())
		return v1.day() > v2.day();
	if (v1.hour() != v2.hour())
		return v1.hour() > v2.hour();
	if (v1.minute() != v2.minute())
		return v1.minute() > v2.minute();
	return v1.second() > v2.second();
}

DateInterval::DateInterval(Calendar &c, Calendar &d)
{
	Calendar later(1, Month::jan, 1, 0, 0, 0);
	Calendar earlier(1, Month::jan, 1, 0, 0, 0);
	int sign = 1;
	if (c > d)
	{
		later = c;
		earlier = d;
	}
	if (d > c)
	{
		later = d;
		earlier = c;
		sign = -1;
	}
	if (!(c > d) && !(d > c))
	{
		yr = 0;
		mh = 0;
		dy = 0;
		hr = 0;
		me = 0;
		sd = 0;
	}
	if (yr != 0 && mh != 0 && dy != 0 && hr != 0 && me != 0 && sd != 0)
	{
		const vector<unsigned int> d_in_m = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		yr = later.year() - earlier.year();
		if (int(later.month()) - int(earlier.month()) < 0)
		{
			--yr;
			mh = int(later.month()) - int(earlier.month()) + 12;
		}
		else
			mh = int(later.month()) - int(earlier.month());
		if ((int(later.day()) - int(earlier.day())) < 0)
		{
			--mh;
			if (mh < 0)
			{
				--yr;
				mh += 12;
			}
			if (leapyear(later.year()) && later.month() == Month::feb)
				dy = later.day() - earlier.day() + 29;
			else
				dy = later.day() - earlier.day() + d_in_m[int(later.month())];
		}
		else
			dy = later.day() - earlier.day();
		if ((int(later.hour()) - int(earlier.hour())) < 0)
		{
			--dy;
			if (dy < 0)
			{
				--mh;
				dy += 24;
				if (mh < 0)
				{
					--yr;
					mh += 12;
				}
			}
			hr = later.hour() - earlier.hour() + 24;
		}
		else
			hr = later.hour() - earlier.hour();
		if ((int(later.minute()) - int(earlier.minute())) < 0)
		{
			--hr;
			if (hr < 0)
			{
				--dy;
				hr += 24;
				if (dy < 0)
				{
					--mh;
					dy += 24;
					if (mh < 0)
					{
						--yr;
						mh += 12;
					}
				}
			}
			me = later.minute() - earlier.minute() + 60;
		}
		else
			me = later.minute() - earlier.minute();
		if ((int(later.second()) - int(earlier.second())) < 0)
		{
			--me;
			if (me < 0)
			{
				--hr;
				if (hr < 0)
				{
					--dy;
					hr += 24;
					if (dy < 0)
					{
						--mh;
						dy += 24;
						if (mh < 0)
						{
							--yr;
							mh += 12;
						}
					}
				}
			}
			sd = later.second() - earlier.second() + 60;
		}
		else
			sd = later.second() - earlier.second();
		yr *= sign;
		mh *= sign;
		dy *= sign;
		hr *= sign;
		me *= sign;
		sd *= sign;
	}
}

DateInterval::DateInterval(const DateInterval &c)
{
	yr = c.yr;
	mh = c.mh;
	dy = c.dy;
	hr = c.hr;
	me = c.me;
	sd = c.sd;
}