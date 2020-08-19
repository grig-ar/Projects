OPENQASM 2.0;
include "qelib1.inc";
qreg q1[5];
creg c1[5];
h q1[0];
h q1[1];
h q1[2];
h q1[3];
h q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
x q1[1];
x q1[3];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
cu1(0.7853981633974483) q1[3],q1[4];
mcx q1[0],q1[1],q1[2],q1[3];
cu1(-0.7853981633974483) q1[3],q1[4];
mcx q1[0],q1[1],q1[2],q1[3];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
u1(0.09817477042468103) q1[2];
cx q1[2],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[2],q1[1];
u1(-0.09817477042468103) q1[1];
cx q1[1],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[1];
u1(0.09817477042468103) q1[1];
cx q1[1],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[1],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[0];
u1(-0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[0];
u1(0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];

cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[0];
u1(-0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[0];
u1(0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
x q1[1];
x q1[3];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
h q1[0];
h q1[1];
h q1[2];
h q1[3];
h q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
x q1[0];
x q1[1];
x q1[2];
x q1[3];
x q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
cu1(0.7853981633974483) q1[3],q1[4];
mcx q1[0],q1[1],q1[2],q1[3];
cu1(-0.7853981633974483) q1[3],q1[4];
mcx q1[0],q1[1],q1[2],q1[3];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
u1(0.09817477042468103) q1[2];
cx q1[2],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[2],q1[1];
u1(-0.09817477042468103) q1[1];
cx q1[1],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[1];
u1(0.09817477042468103) q1[1];
cx q1[1],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[1],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[0];
u1(-0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[0];
u1(0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[1],q1[0];
u1(-0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[2],q1[0];
u1(0.09817477042468103) q1[0];
cx q1[0],q1[4];
u1(-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u1(0.09817477042468103) q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
x q1[0];
x q1[1];
x q1[2];
x q1[3];
x q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
h q1[0];
h q1[1];
h q1[2];
h q1[3];
h q1[4];
barrier q1[0],q1[1],q1[2],q1[3],q1[4];
measure q1[0] -> c1[0];
measure q1[1] -> c1[1];
measure q1[2] -> c1[2];
measure q1[3] -> c1[3];
measure q1[4] -> c1[4];




version 1.0
qubits 5


# sub-circuit for state initialization
	.init
		{ h q[0] | h q[1] | h q[2] | h q[3] | h q[4]}

 # core step of Grover’s algorithm
 # loop with 3 iterations
	.grover(3)
# search for |x> = |0100>
# oracle implementation
		{X q[1] | X q[3]}
		CR q[3], q[4], 0.7854
    
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Cnot q[3], q[0]
   	Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Cnot q[3], q[0]
    Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    
    CR q[3], q[4], -0.7854
    
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Cnot q[3], q[0]
   	Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Cnot q[3], q[0]
    Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    
    Rz q[2], 0.0981
    Cnot q[2], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[4]
    Rz q[4], 0.0981
    Cnot q[2], q[1]
    Rz q[1], -0.0981
    Cnot q[1], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[4]
    Rz q[2], -0.0981
    Cnot q[2], q[1]
    Rz q[1], 0.0981
    Cnot q[1], q[4]
    Rz q[4], -0.0981
    Cnot q[1], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[0]
    Rz q[0], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[0]
    Rz q[0], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[0]
    Rz q[0], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[0]
    Rz q[0], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    
		{X q[1] | X q[3]}
    { h q[0] | h q[1] | h q[2] | h q[3] | h q[4]}
    {X q[0] | X q[1]| X q[2]| X q[3]| X q[4]}
    CR q[3], q[4], 0.7854
    
        Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Cnot q[3], q[0]
   	Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Cnot q[3], q[0]
    Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    
    CR q[3], q[4], -0.7854
    
        Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Cnot q[3], q[0]
   	Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Cnot q[3], q[0]
    Rz q[3], 0.7854
    Cnot q[3], q[1]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    Rz q[3], 0.7854
    Cnot q[2], q[3]
    Rz q[3], -0.7854
    Rz q[3], 1.5708
    Rx q[3], 1.5708
    Rz q[3], 1.5708
    
    Rz q[2], 0.0981
    Cnot q[2], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[4]
    Rz q[4], 0.0981
    Cnot q[2], q[1]
    Rz q[1], -0.0981
    Cnot q[1], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[4]
    Rz q[2], -0.0981
    Cnot q[2], q[1]
    Rz q[1], 0.0981
    Cnot q[1], q[4]
    Rz q[4], -0.0981
    Cnot q[1], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[0]
    Rz q[0], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[0]
    Rz q[0], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[1], q[0]
    Rz q[0], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[2], q[0]
    Rz q[0], 0.0981
    Cnot q[0], q[4]
    Rz q[4], -0.0981
    Cnot q[0], q[4]
    Rz q[4], 0.0981
    
    {X q[0] | X q[1]| X q[2]| X q[3]| X q[4]}
    { h q[0] | h q[1] | h q[2] | h q[3] | h q[4]}
	
	
	
	
	
version 1.0
qubits 5


# sub-circuit for state initialization
	.init
		{ h q[0] | h q[1] | h q[2] | h q[3] | h q[4]}

 # core step of Grover’s algorithm
 # loop with 3 iterations
	.grover(1)
# search for |x> = |0100>
# oracle implementation
		{X q[1] | X q[3]}
    
		Rz q[2], 0.3927
    Rz q[3], 0.3927
    Cnot q[3], q[4]
    Rz q[4], -0.3927
    Cnot q[3], q[4]
    H q[3]
    Cnot q[2], q[3]
    Rz q[3], -0.3927
    Cnot q[2], q[3]
    Cnot q[2], q[1]
    Rz q[1], -0.3927
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Cnot q[2], q[1]
    Rz q[1], 0.3927
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927    
		Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[2], 0.3927       
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    H q[3]
    Rz q[3], -0.3927       
    Rz q[4], 0.3927
    Cnot q[3], q[4]
    Rz q[4], 0.3927
    Cnot q[3], q[4]
    H q[3]
    Cnot q[2], q[3]
    Rz q[3], -0.3927
    Cnot q[2], q[3]
    Cnot q[2], q[1]
    Rz q[1], -0.3927       
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Cnot q[2], q[1]
    Rz q[1], 0.3927       
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927       
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927       
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927       
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927       
    Rz q[2], 0.0982       
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    H q[3]
    X q[3]
    H q[3]
    X q[3]
    Rz q[3], 0.3927       
    Rz q[4], -0.3927
    Cnot q[2], q[4]
    Rz q[4], -0.0982 
    Cnot q[2], q[4]
    Cnot q[2], q[1]
    Rz q[1], -0.0982
    Rz q[4], 0.0982 
    Cnot q[1], q[4]
    Rz q[4], 0.0982
    Cnot q[1], q[4]
    Cnot q[2], q[1]
    Rz q[1], 0.0982
    Rz q[4], -0.0982 
    Cnot q[1], q[4]
    Rz q[4], -0.0982 
    Cnot q[1], q[4]
    Cnot q[1], q[0]
    Rz q[0], -0.0982
    Rz q[4], 0.0982 
    Cnot q[0], q[4]
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Cnot q[2], q[0]
    Rz q[0], 0.0982
    Rz q[4], -0.0982 
    Cnot q[0], q[4]
    Rz q[4], -0.0982 
    Cnot q[0], q[4]
    Cnot q[1], q[0]
    Rz q[0], -0.0982 
    X q[1]
    H q[1]
    X q[1]
    Rz q[4], 0.0982 
    Cnot q[0], q[4]
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Cnot q[2], q[0]
    Rz q[0], 0.0982
    H q[2]
    X q[2]
    Rz q[2], 0.3927
    Rz q[4], -0.0982 
    Cnot q[0], q[4]
    Rz q[4], -0.0982 
    Cnot q[0], q[4]
    H q[0]
    X q[0]
    Rz q[4], 0.0982 
    H q[4]
    X q[4]
    Cnot q[3], q[4]
    Rz q[4], -0.3927
    Cnot q[3], q[4]
    H q[3]
    Cnot q[2], q[3]
    Rz q[3], -0.3927
    Cnot q[2], q[3]
    Cnot q[2], q[1]
    Rz q[1], -0.3927
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Cnot q[2], q[1]
    Rz q[1], 0.3927
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[2], 0.3927
    Rz q[3], -0.3927
		Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    H q[3]
    Rz q[3], -0.3927
    Rz q[4], 0.3927
    Cnot q[1], q[4]
    Rz q[4], 0.3927
    Cnot q[3], q[4]
    H q[3]
    Cnot q[2], q[3]
    Rz q[3], -0.3927
    Cnot q[2], q[3]
    Cnot q[2], q[1]
    Rz q[1], -0.3927
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Rz q[3], 0.3927
    Cnot q[1], q[3]
    Cnot q[2], q[1]
    Rz q[1], 0.3927
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Rz q[3], -0.3927
    Cnot q[1], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Cnot q[1], q[0]
    Rz q[0], -0.3927
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    Cnot q[0], q[3]
    Cnot q[2], q[0]
    Rz q[0], 0.3927
    Rz q[2], 0.0982
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], -0.3927
    Cnot q[0], q[3]
    Rz q[3], 0.3927
    H q[3]
    X q[3]
    H q[3]
    Rz q[4], -0.3927
    Cnot q[2], q[4]
    Rz q[4], -0.0982
    Cnot q[2], q[4]
    Cnot q[2], q[1]
    Rz q[1], -0.0982
    Rz q[4], 0.0982
    Cnot q[1], q[4]
    Rz q[4], 0.0982
    Cnot q[1], q[4]
    Cnot q[2], q[1]
    Rz q[1], 0.0982
    Rz q[4], -0.0982
    Cnot q[1], q[4]
    Rz q[4], -0.0982
    Cnot q[1], q[4]
    Cnot q[1], q[0]
    Rz q[0], -0.0982
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Cnot q[2], q[0]
    Rz q[0], 0.0982
    Rz q[4], -0.0982
    Cnot q[0], q[4]
    Rz q[4], -0.0982
    Cnot q[0], q[4]
    Cnot q[1], q[0]
    Rz q[0], -0.0982
    X q[1]
    H q[1]
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Rz q[4], 0.0982
    Cnot q[0], q[4]
    Cnot q[2], q[0]
    Rz q[0], 0.0982
    X q[2]
    H q[2]
    Rz q[4], -0.0982
    Cnot q[0], q[4]
    Rz q[4], -0.0982
    Cnot q[0], q[4]
    X q[0]
    H q[0]
    Rz q[4], 0.0982
    X q[4]
    H q[4]
    
    
    
    
    
    
    
    
