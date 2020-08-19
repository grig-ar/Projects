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

/*
version 0.3.1 (09.11.17)
*/

enum class Month
{
	jan = 1, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec
};

class Calendar
{
public:
	class Invalid { };
	Calendar(unsigned int yy, Month mm, unsigned int dd, unsigned int hh, unsigned int mmin, unsigned int ss);
	Calendar(const Calendar &c);
	Calendar(unsigned int yy, Month mm, unsigned int dd);
	Calendar(unsigned int hh, unsigned int mmin, unsigned int ss);
	Calendar();
	Calendar& operator++(); // prefix increment
	Calendar operator++(int); // postfix increment
	Calendar& operator--(); // prefix decrement
	Calendar operator--(int); // postfix decrement
	std::string formatDate(std::string format);
	void normalize(unsigned int yy, Month mm, unsigned int dd, unsigned int hh, unsigned int mmin, unsigned int ss);
	std::string toString() const;
	unsigned int day() const { return d; }
	Month month() const { return m; }
	unsigned int year() const { return y; }
	unsigned int hour() const { return h; }
	unsigned int minute() const { return min; }
	unsigned int second() const { return s; }
	Calendar add_day(int n) const;
	Calendar add_month(int n) const;
	Calendar add_year(int n) const;
	Calendar add_hour(int n) const;
	Calendar add_minute(int n) const;
	Calendar add_second(int n) const;
	Calendar& operator= (const Calendar &c)
	{
		this->y = c.y, this->m = c.m, this->d = c.d, this->h = c.h, this->min = c.min, this->s = c.s;
		return *this;
	}
private:
	unsigned int y;
	Month m;
	unsigned int d;
	unsigned int h;
	unsigned int min;
	unsigned int s;
};

class DateInterval
{
public:
	DateInterval(Calendar &c, Calendar &d);
	DateInterval(const DateInterval &c);
	std::string toString() const;
	int di_day() const { return dy; }
	int di_month() const { return mh; }
	int di_year() const { return yr; }
	int di_hour() const { return hr; }
	int di_minute() const { return me; }
	int di_second() const { return sd; }
	DateInterval& operator= (const DateInterval &c)
	{
		this->yr = c.yr, this->mh = c.mh, this->dy = c.dy, this->hr = c.hr, this->me = c.me, this->sd = c.sd;
		return *this;
	}
private:
	int yr;
	int mh;
	int dy;
	int hr;
	int me;
	int sd;
};

bool is_date(int y, Month m, int d, int h, int min, int s);
bool leapyear(unsigned int y);
//std::string itos(int a);
//void parse(int n, std::vector<int>& vec);
bool operator== (const Calendar& a, const Calendar& b);
bool operator!= (const Calendar& a, const Calendar& b);
bool operator> (Calendar& v1, Calendar& v2);
Calendar operator+ (const Calendar &c, const DateInterval &interv);
Calendar& operator+= (Calendar &c, const DateInterval &interv);
Calendar& operator-= (Calendar &c, const DateInterval &interv);
Calendar operator- (Calendar &c, const DateInterval &interv);
DateInterval get_interval(Calendar& first, Calendar& another);
Calendar add_interval(const Calendar& c, const DateInterval& interv);


