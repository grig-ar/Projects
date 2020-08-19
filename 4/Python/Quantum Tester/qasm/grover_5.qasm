OPENQASM 2.0;
include "qelib1.inc";
qreg q1[5];
creg c1[5];
u2(0,3.141592653589793) q1[0];
u2(0,3.141592653589793) q1[1];
u3(3.141592653589793,0,3.141592653589793) q1[1];
u2(0,3.141592653589793) q1[2];
u2(0,3.141592653589793) q1[3];
u3(3.141592653589793,0,3.141592653589793) q1[3];
u1(0.39269908169872414) q1[3];
u2(0,3.141592653589793) q1[4];
cx q1[3],q1[4];
u1(-0.39269908169872414) q1[4];
cx q1[3],q1[4];
h q1[3];
mcu1(3.141592653589793) q1[0],q1[1],q1[2],q1[3];
h q1[3];
u1(-0.39269908169872414) q1[3];
u1(0.39269908169872414) q1[4];
cx q1[3],q1[4];
u1(0.39269908169872414) q1[4];
cx q1[3],q1[4];
h q1[3];
mcu1(3.141592653589793) q1[0],q1[1],q1[2],q1[3];
u3(0,0,0.09817477042468103) q1[2];
h q1[3];
u3(3.141592653589793,0,3.141592653589793) q1[3];
u2(0,3.141592653589793) q1[3];
u3(3.141592653589793,0,3.141592653589793) q1[3];
u1(0.39269908169872414) q1[3];
u1(-0.39269908169872414) q1[4];
cx q1[2],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[2],q1[4];
cx q1[2],q1[1];
u3(0,0,-0.09817477042468103) q1[1];
u3(0,0,0.09817477042468103) q1[4];
cx q1[1],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[1],q1[4];
cx q1[2],q1[1];
u3(0,0,0.09817477042468103) q1[1];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[1],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[1],q1[4];
cx q1[1],q1[0];
u3(0,0,-0.09817477042468103) q1[0];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[2],q1[0];
u3(0,0,0.09817477042468103) q1[0];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[1],q1[0];
u3(0,0,-0.09817477042468103) q1[0];
u3(3.141592653589793,0,3.141592653589793) q1[1];
u2(0,3.141592653589793) q1[1];
u3(3.141592653589793,0,3.141592653589793) q1[1];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[2],q1[0];
u3(0,0,0.09817477042468103) q1[0];
u2(0,3.141592653589793) q1[2];
u3(3.141592653589793,0,3.141592653589793) q1[2];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u2(0,3.141592653589793) q1[0];
u3(3.141592653589793,0,3.141592653589793) q1[0];
u3(0,0,0.09817477042468103) q1[4];
u2(0,3.141592653589793) q1[4];
u3(3.141592653589793,0,3.141592653589793) q1[4];
cx q1[3],q1[4];
u1(-0.39269908169872414) q1[4];
cx q1[3],q1[4];
h q1[3];
mcu1(3.141592653589793) q1[0],q1[1],q1[2],q1[3];
h q1[3];
u1(-0.39269908169872414) q1[3];
u1(0.39269908169872414) q1[4];
cx q1[3],q1[4];
u1(0.39269908169872414) q1[4];
cx q1[3],q1[4];
h q1[3];
mcu1(3.141592653589793) q1[0],q1[1],q1[2],q1[3];
u3(0,0,0.09817477042468103) q1[2];
h q1[3];
u3(3.141592653589793,0,3.141592653589793) q1[3];
u2(0,3.141592653589793) q1[3];
u1(-0.39269908169872414) q1[4];
cx q1[2],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[2],q1[4];
cx q1[2],q1[1];
u3(0,0,-0.09817477042468103) q1[1];
u3(0,0,0.09817477042468103) q1[4];
cx q1[1],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[1],q1[4];
cx q1[2],q1[1];
u3(0,0,0.09817477042468103) q1[1];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[1],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[1],q1[4];
cx q1[1],q1[0];
u3(0,0,-0.09817477042468103) q1[0];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[2],q1[0];
u3(0,0,0.09817477042468103) q1[0];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[1],q1[0];
u3(0,0,-0.09817477042468103) q1[0];
u3(3.141592653589793,0,3.141592653589793) q1[1];
u2(0,3.141592653589793) q1[1];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,0.09817477042468103) q1[4];
cx q1[0],q1[4];
cx q1[2],q1[0];
u3(0,0,0.09817477042468103) q1[0];
u3(3.141592653589793,0,3.141592653589793) q1[2];
u2(0,3.141592653589793) q1[2];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(0,0,-0.09817477042468103) q1[4];
cx q1[0],q1[4];
u3(3.141592653589793,0,3.141592653589793) q1[0];
u2(0,3.141592653589793) q1[0];
u3(0,0,0.09817477042468103) q1[4];
u3(3.141592653589793,0,3.141592653589793) q1[4];
u2(0,3.141592653589793) q1[4];
measure q1[0] -> c1[0];
measure q1[1] -> c1[1];
measure q1[2] -> c1[2];
measure q1[3] -> c1[3];
measure q1[4] -> c1[4];
