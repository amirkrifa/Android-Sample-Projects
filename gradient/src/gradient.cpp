
#include <iostream>
#include<map>
#include<string>
#include <fstream>
#include <cstdlib>
#include <cmath>
#include <cassert>
using namespace std;

// map the maintains the elapsed time fo each message
map<int, double> ri_map;

// map that will store the ni optimal vector
map<int, double> ni_vector;
map<int, double> gamma_vector;
double beta;

// Number of messages in the network
double k = 400;
// The number of nodes in the network
double l = 70;
// The buffer capacity of each node
double b = 20;
// The ttl of each generated message
double ttl = 3600;
// The average meeting time
double amt = 100;
//Lambda
double lambda = 1/amt;

int maxNumberIterations = 2000;
double step = 1;

// Function to read R_i values from a file and load them to a map
void loadRi(string filePath)
{
	cout<<"Loading Ri vector: "<<endl;
	ifstream tmpFile;
	tmpFile.open(filePath.c_str());
	string line;
	int lineNumber = 0;

	if (tmpFile.is_open())
	{
		while (!tmpFile.eof())
		{
			tmpFile >> line;
			// The R_i map
			ri_map.insert(make_pair<int, int>(lineNumber, ttl - atoi(line.c_str())));
			//cout<<"R "<<lineNumber<<" "<<ri_map[lineNumber]<<endl;
			// The n_i vector which will be used for the calculation of the gradient
			//cout<<"R_"<<lineNumber<<" = "<<ri_map[lineNumber]<<endl;
			ni_vector.insert(make_pair<int, double>(lineNumber, 1));
			lineNumber ++;
		}
	}

	//cout<<"LineNumber: "<<lineNumber<<endl;
	k = lineNumber;
	// init beta
	beta = 1;

	// Adding the parameters gamm_i
	for(int i = 0; i < k; i++)
	{
		gamma_vector.insert(make_pair<int, double>(i, 1));
	}

	cout<<"Ri map loaded, size: "<<ri_map.size()<<endl;
	cout<<"ni_vector_size: "<<ni_vector.size()<<endl;
	cout<<"gamma_vector size: "<<gamma_vector.size()<<endl;
	cout<<"K: "<<k<<endl;
	tmpFile.close();
}

double d_f_ni(double lambda, double r_i, double n_i, double beta, double gamma_i)
{
	//cout<<"ri: "<<r_i<<" ni: "<<n_i<<" beta: "<<beta<<" gamma_i: "<<gamma_i<<endl;
	double tmp =  lambda*r_i*exp(-1*lambda*r_i*n_i) - beta - gamma_i;
	//cout<<"calculated d_f_ni: "<<tmp<<endl;
	return tmp;
}

double d_f_beta(double n_i)
{
	double tmp = l*b - n_i;
	//cout<<"calculated d_f_beta: "<<tmp<<endl;
	return tmp;
}

double d_f_gamma_i(double n_i)
{
	double tmp = l - n_i;
	//cout<<"calculated d_f_gammai: "<<tmp<<endl;
	return tmp;
}

void calculate_next_vector(map<int, double> &ni_tmp, map<int, double> &gamma_tmp, double  new_beta)
{
	double sum_ni = 0;
	// Updating the value of n_i
	int i = 0;
	for(; i < k; i++)
	{
		sum_ni += ni_vector[i];

		double tmp = ni_vector[i] + step*d_f_ni(lambda, ri_map[i], ni_vector[i], beta,gamma_vector[i]);
		ni_tmp.insert(make_pair<int, double>(i, tmp));
	}

	// Updating the value of beta
	new_beta = beta + step*d_f_beta(sum_ni);
	// Updating the gamma vector
	for(unsigned int j = 0; j < k; j++)
	{
		double tmp = ni_vector[j] + step*d_f_gamma_i(ni_vector[j]);
		//cout<<"tmp gamma: "<<tmp<<endl;
		gamma_tmp.insert(make_pair<int, double>(j, tmp));
	}

	//assert(ni_tmp.size() == ni_vector.size());
	//assert(gamma_tmp.size() == gamma_vector.size());

}

void update_current_vectors(map<int, double> &ni_tmp, map<int, double> &gamma_tmp, double new_beta)
{
	//assert(ni_vector.size() == ni_tmp.size());
	//assert(gamma_vector.size() == gamma_tmp.size());
	//assert(ni_vector.size() == gamma_vector.size());

	for(unsigned int i = 0; i < ni_vector.size(); i++)
	{
		ni_vector[i] = ni_tmp[i];
		gamma_vector[i] = gamma_tmp[i];
	}
	ni_tmp.clear();
	gamma_tmp.clear();
	beta = new_beta;
}

double euclidean_distance_between(map<int, double> &ni_tmp, map<int, double> &gamma_tmp, double new_beta)
{
	//assert(ni_vector.size() == ni_tmp.size());
	//assert(gamma_vector.size() == gamma_tmp.size());
	//assert(ni_vector.size() == gamma_vector.size());

	double d = 0;
	for(unsigned int i = 0; i < ni_vector.size(); i++)
	{
		d += pow((ni_vector[i] - ni_tmp[i]), 2);
		d += pow((gamma_vector[i] - gamma_tmp[i]), 2);
	}

	d += pow(beta - new_beta, 2);

	return sqrt(d);
}

void write_vector_to_file(string fileName, map<int, double> v)
{
	cout<<"Writing optimal vector of size: "<<v.size()<<endl;
	ofstream tmpFile;
	tmpFile.open(fileName.c_str());
	int sum = 0;
	for(unsigned int i = 0; i < v.size(); i++)
	{
		cout<<"n_"<<i<<" = "<<v[i]<<endl;
		tmpFile << floor(v[i])<<endl;
		sum += ceil(v[i]);
	}
	//cout<<"Sum of number of copies: "<<sum<<endl;
	tmpFile.close();
}

int main(int argc, char ** argv)
{
	cout<<argc<<endl;
	if(argc >= 2)
	{
		if(argc == 3)
		{
			maxNumberIterations = atoi(argv[2]);
		}

		if(argc == 4)
		{
			step = atof(argv[3]);
		}

		cout<<" Creation the optimal vector starting from the file: "+ string(argv[1])<<endl;
		string fileName;

		fileName.append(argv[1]);

		loadRi(fileName);

		int i = 0;
		while(true)
		{
			map<int, double> ni_tmp, gamma_tmp;
			double new_beta = 0;
			calculate_next_vector(ni_tmp, gamma_tmp, new_beta);
			double dist = euclidean_distance_between(ni_tmp, gamma_tmp, beta);
			update_current_vectors(ni_tmp, gamma_tmp, new_beta);
			cout<<"Current distance: "<<dist<<endl;
			if(i % maxNumberIterations == 0)
				write_vector_to_file("optimal"+fileName, ni_vector);
			if(dist == 0)
				break;
			i++;
		}

		write_vector_to_file("optimal"+fileName, ni_vector);
		return 0;
	}else
	{
		cerr<<"Invalid arguments, please provide the vector to parse"<<endl;
		return -1;
	}
}
