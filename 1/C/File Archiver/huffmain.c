#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <stdint.h>
#include <time.h>  
#include <sys/timeb.h>  
#include <sys/types.h>  
#include <sys/stat.h> 
#include <errno.h> 
#include <Windows.h>
#include <sys/utime.h>  
#include <direct.h>

unsigned char *codedTree, *codedStr;

typedef struct {
	unsigned char data[65536];
	unsigned char remainder;
	unsigned short index;
	struct block * next;
}block;

block * headCodedBlock;

typedef struct {
	unsigned short head_crc;
	unsigned short head_type;
	unsigned char head_flags;
	unsigned char head_size;
}head_marker;

typedef struct {
	int len;
	int code[256];
}huffCode;
//typedef huffCode[256] huffTable;

typedef struct {
	unsigned char head_type;//1
	unsigned char head_flags;//1
	unsigned char head_size;//1
	unsigned int pack_size;//4
	unsigned char remainder;//1
	unsigned int unpack_size;//4
	unsigned short name_size;//2
	unsigned int file_crc;//4
	unsigned int f_time;//4
	unsigned int attrib;//4
	unsigned int high_pack_size;//4
	unsigned int high_unpack_size;//4
	unsigned short alphLen;//2
	unsigned int lenCodedTree;//4
	unsigned char remainderTree;//1
	unsigned short head_crc;//2
}head_file;

struct MinHeapNode
{
	unsigned char data;  // One of the input characters
	unsigned freq;  // Frequency of the character
	struct MinHeapNode *left, *right, *parent; // Left and right child of this node and parent
};

struct MinHeap
{
	unsigned size;    // Current size of min heap
	unsigned capacity;   // capacity of min heap
	struct MinHeapNode **array;  // Attay of minheap node pointers
};

typedef struct {
	unsigned char * fileName;
	unsigned int ofset;
	unsigned int len;
	struct filesListNode * next;
} filesListNode;


const unsigned int Crc32Table[256] = {
0x00000000, 0x77073096, 0xEE0E612C, 0x990951BA,
0x076DC419, 0x706AF48F, 0xE963A535, 0x9E6495A3,
0x0EDB8832, 0x79DCB8A4, 0xE0D5E91E, 0x97D2D988,
0x09B64C2B, 0x7EB17CBD, 0xE7B82D07, 0x90BF1D91,
0x1DB71064, 0x6AB020F2, 0xF3B97148, 0x84BE41DE,
0x1ADAD47D, 0x6DDDE4EB, 0xF4D4B551, 0x83D385C7,
0x136C9856, 0x646BA8C0, 0xFD62F97A, 0x8A65C9EC,
0x14015C4F, 0x63066CD9, 0xFA0F3D63, 0x8D080DF5,
0x3B6E20C8, 0x4C69105E, 0xD56041E4, 0xA2677172,
0x3C03E4D1, 0x4B04D447, 0xD20D85FD, 0xA50AB56B,
0x35B5A8FA, 0x42B2986C, 0xDBBBC9D6, 0xACBCF940,
0x32D86CE3, 0x45DF5C75, 0xDCD60DCF, 0xABD13D59,
0x26D930AC, 0x51DE003A, 0xC8D75180, 0xBFD06116,
0x21B4F4B5, 0x56B3C423, 0xCFBA9599, 0xB8BDA50F,
0x2802B89E, 0x5F058808, 0xC60CD9B2, 0xB10BE924,
0x2F6F7C87, 0x58684C11, 0xC1611DAB, 0xB6662D3D,
0x76DC4190, 0x01DB7106, 0x98D220BC, 0xEFD5102A,
0x71B18589, 0x06B6B51F, 0x9FBFE4A5, 0xE8B8D433,
0x7807C9A2, 0x0F00F934, 0x9609A88E, 0xE10E9818,
0x7F6A0DBB, 0x086D3D2D, 0x91646C97, 0xE6635C01,
0x6B6B51F4, 0x1C6C6162, 0x856530D8, 0xF262004E,
0x6C0695ED, 0x1B01A57B, 0x8208F4C1, 0xF50FC457,
0x65B0D9C6, 0x12B7E950, 0x8BBEB8EA, 0xFCB9887C,
0x62DD1DDF, 0x15DA2D49, 0x8CD37CF3, 0xFBD44C65,
0x4DB26158, 0x3AB551CE, 0xA3BC0074, 0xD4BB30E2,
0x4ADFA541, 0x3DD895D7, 0xA4D1C46D, 0xD3D6F4FB,
0x4369E96A, 0x346ED9FC, 0xAD678846, 0xDA60B8D0,
0x44042D73, 0x33031DE5, 0xAA0A4C5F, 0xDD0D7CC9,
0x5005713C, 0x270241AA, 0xBE0B1010, 0xC90C2086,
0x5768B525, 0x206F85B3, 0xB966D409, 0xCE61E49F,
0x5EDEF90E, 0x29D9C998, 0xB0D09822, 0xC7D7A8B4,
0x59B33D17, 0x2EB40D81, 0xB7BD5C3B, 0xC0BA6CAD,
0xEDB88320, 0x9ABFB3B6, 0x03B6E20C, 0x74B1D29A,
0xEAD54739, 0x9DD277AF, 0x04DB2615, 0x73DC1683,
0xE3630B12, 0x94643B84, 0x0D6D6A3E, 0x7A6A5AA8,
0xE40ECF0B, 0x9309FF9D, 0x0A00AE27, 0x7D079EB1,
0xF00F9344, 0x8708A3D2, 0x1E01F268, 0x6906C2FE,
0xF762575D, 0x806567CB, 0x196C3671, 0x6E6B06E7,
0xFED41B76, 0x89D32BE0, 0x10DA7A5A, 0x67DD4ACC,
0xF9B9DF6F, 0x8EBEEFF9, 0x17B7BE43, 0x60B08ED5,
0xD6D6A3E8, 0xA1D1937E, 0x38D8C2C4, 0x4FDFF252,
0xD1BB67F1, 0xA6BC5767, 0x3FB506DD, 0x48B2364B,
0xD80D2BDA, 0xAF0A1B4C, 0x36034AF6, 0x41047A60,
0xDF60EFC3, 0xA867DF55, 0x316E8EEF, 0x4669BE79,
0xCB61B38C, 0xBC66831A, 0x256FD2A0, 0x5268E236,
0xCC0C7795, 0xBB0B4703, 0x220216B9, 0x5505262F,
0xC5BA3BBE, 0xB2BD0B28, 0x2BB45A92, 0x5CB36A04,
0xC2D7FFA7, 0xB5D0CF31, 0x2CD99E8B, 0x5BDEAE1D,
0x9B64C2B0, 0xEC63F226, 0x756AA39C, 0x026D930A,
0x9C0906A9, 0xEB0E363F, 0x72076785, 0x05005713,
0x95BF4A82, 0xE2B87A14, 0x7BB12BAE, 0x0CB61B38,
0x92D28E9B, 0xE5D5BE0D, 0x7CDCEFB7, 0x0BDBDF21,
0x86D3D2D4, 0xF1D4E242, 0x68DDB3F8, 0x1FDA836E,
0x81BE16CD, 0xF6B9265B, 0x6FB077E1, 0x18B74777,
0x88085AE6, 0xFF0F6A70, 0x66063BCA, 0x11010B5C,
0x8F659EFF, 0xF862AE69, 0x616BFFD3, 0x166CCF45,
0xA00AE278, 0xD70DD2EE, 0x4E048354, 0x3903B3C2,
0xA7672661, 0xD06016F7, 0x4969474D, 0x3E6E77DB,
0xAED16A4A, 0xD9D65ADC, 0x40DF0B66, 0x37D83BF0,
0xA9BCAE53, 0xDEBB9EC5, 0x47B2CF7F, 0x30B5FFE9,
0xBDBDF21C, 0xCABAC28A, 0x53B39330, 0x24B4A3A6,
0xBAD03605, 0xCDD70693, 0x54DE5729, 0x23D967BF,
0xB3667A2E, 0xC4614AB8, 0x5D681B02, 0x2A6F2B94,
0xB40BBE37, 0xC30C8EA1, 0x5A05DF1B, 0x2D02EF8D
};


int copyFiles(char *sourceName, unsigned int sourceSize, char *destName) {
	unsigned char buffer[65535];
	unsigned short lenBuffer = 65535;
	unsigned int ofset = 0;
	FILE * source;
	FILE * dest;
	errno_t err;
	err = fopen_s(&source, sourceName, "rb");
	if (err !=0) {
		printf("\nSource file %s not opened", sourceName);
		return -1;
	}
	err = fopen_s(&dest, destName, "wb");
	if (err != 0) {
		printf("\nDestination file %s not opened", destName);
		return -1;
	}
	while (ofset < sourceSize) {
		fseek(source, ofset, SEEK_SET);
		if ((sourceSize-ofset)<lenBuffer) lenBuffer = sourceSize - ofset;
		if (fread(buffer, 1, lenBuffer, source) != lenBuffer) {
			printf("\nRead temp file error");
			return -1;
		}
		if(fwrite(buffer, 1, lenBuffer, dest)!=lenBuffer) {
			printf("\nWrite destination file error");
			return -1;
		}
		ofset += lenBuffer;
	}
	fclose(source);
	fclose(dest);
	return 0;
}

void printProgress(unsigned int current, unsigned int all){
	float t=(float)current*100/all;
	if (current>0) printf("\b\b\b\b\b\b\b");
	printf("                              ");
	printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
	printf("%6.1f%%",t);
	return;
}

void blockWrite(block * head, FILE * f) {
	block * currentNode = head;
	while (currentNode) {
		fwrite(currentNode->data, 1, currentNode->index, f);
		currentNode = currentNode->next;
	}
	return;
}

int countFilesInList(filesListNode * head) {
	int k = 0;
	filesListNode * currentFile=head;
	while (currentFile) {
		k++;
		currentFile = currentFile->next;
	}
	return k;
}



char * deletePath(char * source){
	char * dest;
	unsigned int i,j=0;
	for (i=0;i<strlen(source);i++)
		if(source[i]=='\\') j=i;
	if (j > 0) {
		dest = (char *)malloc(strlen(source) - j + 2);
		for (i = j + 1; i < strlen(source); i++) dest[i - j - 1] = source[i];
		dest[i - j - 1] = '\0';
		return dest;
	}
	else return source;
}




unsigned int Crc32(const unsigned char *buf, size_t len)
{
	unsigned int crc = 0xFFFFFFFF;
	while (len--)
		crc = (crc >> 8) ^ Crc32Table[(crc ^ *buf++) & 0xFF];
	return crc ^ 0xFFFFFFFF;
}

unsigned int Crc32File(FILE *f, unsigned int size)
{
	unsigned int crc = 0xFFFFFFFF, ofset=0;
	unsigned char * c;
	c = (char *)malloc(1);
	while (ofset <= size) {
		fseek(f, ofset, SEEK_SET);
		fread(c, 1, 1, f);
		crc = (crc >> 8) ^ Crc32Table[(crc ^ c[0]) & 0xFF];
		ofset++;
	}	
	return crc ^ 0xFFFFFFFF;
}

filesListNode * readListFiles(FILE * arc) {
	filesListNode * head;
	filesListNode * currentFile;
	filesListNode * temp;
	unsigned int sz, ofset = 0, crc, /*tempCRC,*/ packSize, alphLen, lenCodedTree;
	unsigned char * str;
	unsigned short nameSize;
	head = (filesListNode *)malloc(sizeof(filesListNode));
	head->ofset = 0;
	head->next = NULL;
	currentFile = (filesListNode *)malloc(sizeof(filesListNode));
	currentFile->next = NULL;
	temp = (filesListNode *)malloc(sizeof(filesListNode));
	temp->next = NULL;
	fseek(arc, 0L, SEEK_END);
	sz = ftell(arc);
	fseek(arc, 0L, SEEK_SET);
	if (!(str = (char *)malloc((7) * sizeof(char))))
		printf("\nAllocation memory error\n");
	else {
		fread(str, 1, 6, arc);
		if ((str[0] != 0x48) || (str[1] != 0x55) || (str[2] != 0x46) || (str[3] != 0x21) || (str[4] != 0xBB) || (str[5] != 0x48)) {
			printf("\nUnknown file format or bad header");
		}
		else {
			ofset += 6;
			while (ofset < sz) {
				temp->ofset = ofset;
				fseek(arc, ofset, SEEK_SET);
				fread(str, 1, 1, arc);
				fseek(arc, ofset, SEEK_SET);
				switch (str[0]) {
				case 0x60:
					fread(str, 1, 11, arc);
					ofset += 11;
					fseek(arc, ofset, SEEK_SET);
					crc = (str[9] << 8) + str[10];
					if (crc != (Crc32(str, 9) & 0x0000FFFF)) {
						printf("\nHead of block corrupted\n");
						return NULL;
					}
					nameSize = (str[5] << 8) + str[6];
					temp->fileName = (char *)malloc(nameSize + 1);
					fread(temp->fileName, 1, nameSize, arc);
					temp->fileName[nameSize] = '\0';
					ofset += nameSize;
					crc = (str[7] << 8) + str[8];
					if (crc != (Crc32(temp->fileName, nameSize) & 0x0000FFFF)) {
						printf("\nArchive corrupted\n");
						return NULL;
					}
					temp->fileName[nameSize] = '\0';
					temp->len = ofset - temp->ofset;
					break;
				case 0x61:
					if (!(str = (char *)malloc(16))) {
						printf("\nAllocation memory error\n");
						return NULL;
					}
					fread(str, 1, 16, arc);
					ofset += 16;
					crc = (str[14] << 8) + str[15];
					//tempCRC = ;
					if (crc != (Crc32(str, 14) & 0x0000FFFF)) {
						printf("\nHead of block corrupted\n");
						return NULL;
					}
					nameSize = (str[10] << 8) + str[11];
					temp->fileName = (char *)malloc(nameSize + 1);
					fseek(arc, ofset, SEEK_SET);
					fread(temp->fileName, 1, nameSize, arc);
					ofset += nameSize;
					crc = (str[12] << 8) + str[13];
					if (crc != (Crc32(temp->fileName, nameSize) & 0x0000FFFF)) {
						printf("\nArchive corrupted\n");
						return NULL;
					}
					temp->len = ofset - temp->ofset;
					break;
				case 0x62:
					if (!(str = (char *)malloc(34))) {
						printf("\nAllocation memory error\n");
						return NULL;
					}
					fseek(arc, ofset, SEEK_SET);
					fread(str, 1, 34, arc);
					ofset += 34;
					crc = (str[32] << 8) + str[33];
					if (crc != (Crc32(str, 32) & 0x0000FFFF)) {
						printf("\nHead of block corrupted\n");
						return NULL;
					}
					packSize = (str[2] << 24) + (str[3] << 16) + (str[4] << 8) + str[5];
					nameSize = (str[11] << 8) + str[12];
					alphLen = (str[25] << 8) + str[26];
					lenCodedTree = (str[27] << 24) + (str[28] << 16) + (str[29] << 8) + str[30];
					temp->fileName = (char *)malloc(nameSize + 1);
					fread(temp->fileName, 1, nameSize, arc);
					temp->fileName[nameSize] = '\0';
					ofset += nameSize;
					ofset += alphLen;
					ofset += lenCodedTree;
					ofset += packSize;
					temp->len = ofset - temp->ofset;
					break;
				case 0x63:
					fseek(arc, ofset, SEEK_SET);
					if (!(str = (char *)malloc(28))) {
						printf("\nAllocation memory error\n");
						return NULL;
					}
					fread(str, 1, 27, arc);
					if ((str[1] != 0x02) || (str[2] != 0x1B)) {
						printf("\nArchive corrupted\n");
						return 0;
					}
					packSize = (str[3] << 24) + (str[4] << 16) + (str[5] << 8) + str[6];
					nameSize = (str[11] << 8) + str[12];
					crc = (str[25] << 8) + str[26];
					if (crc != (Crc32(str, 25) & 0x0000FFFF)) {
						printf("\nArchive corrupted\n");
						return NULL;
					}
					ofset += 28;
					temp->fileName = (char *)malloc(nameSize + 1);
					fseek(arc, ofset, SEEK_SET);
					fread(temp->fileName, 1, nameSize, arc);
					temp->fileName[nameSize] = '\0';
					ofset += nameSize;
					ofset += packSize;
					temp->len = ofset - temp->ofset;
					break;
				}
				//ofset++;
				if (head->ofset == 0) {
					currentFile->ofset = temp->ofset;
					currentFile->fileName = temp->fileName;
					head = currentFile;
				}
				else {
					currentFile->next = temp;
					currentFile = currentFile->next;
				}
			}
		}
	}
	return head;
}

int deleteFileFromArchive(char * arcName, char * fileName)
{
	int res, amountFiles = 0;
	char answer[3];
	FILE *arc, *tmp;
	filesListNode * head, *currFile;
	unsigned int ofset = 0, len = 0, sz = 0;
	unsigned char * str;
	char tmpfile[L_tmpnam_s];
	if ((_access(arcName, 0)) == 0) {
		res = fopen_s(&arc, arcName, "rb");
		if (res != 0) {
			printf("\nArchive open error\n");
			return -1;
		}
		else {
			tmpnam_s(tmpfile, L_tmpnam_s);
			res = fopen_s(&tmp, tmpfile, "wb");
			head = readListFiles(arc);
			if (head == NULL) {
				return -1;
			}
			fseek(arc, 0L, SEEK_END);
			sz = ftell(arc);
			amountFiles = countFilesInList(head);
			if ((amountFiles == 1) && (fileName == head->fileName)) {
				printf("\nArchive %s will be deleted. Are you sure? (y/n)", arcName);
				scanf_s("%s", answer, 3);
				if ((answer[0] == 'y') || (answer[0] == 'Y')) {
					if (remove(arcName) == -1) {
						printf("\nCould not delete %s\n", arcName);
						return -1;
					}
					else {
						printf("\nDeleted %s\n", arcName);
						return 0;
					}
				}
			}
			if ((amountFiles == 1) && (fileName != head->fileName)) {
				printf("File %s not found in archive %s", fileName, arcName);
				return -1;
			}
			currFile = head;
			while (currFile) {
				if (fileName == currFile->fileName) {
					ofset = currFile->ofset;
					len = currFile->len;
					break;
				}
				currFile = currFile->next;
			}
			if (ofset != 0) {
				if (!(str = (char *)malloc(ofset))) {
					printf("Allocation memory error\n");
					return -1;
				}
				fseek(arc, 0L, SEEK_SET);
				fread(str, 1, ofset - 1, arc);
				fwrite(str, 1, ofset - 1, tmp);
				fseek(arc, ofset + len, SEEK_SET);
				if (!(str = (char *)malloc(sz - ofset - len + 1))) {
					printf("Allocation memory error\n");
					return -1;
				}
				fread(str, 1, ofset + len, arc);
				fwrite(str, 1, sz - ofset - len, tmp);
				fclose(arc);
				fseek(tmp, 0L, SEEK_END);
				sz = ftell(tmp);
				fclose(tmp);
				copyFiles(tmpfile, sz, arcName);
				remove(tmpfile);
			}
			else {
				printf("File %s not found in archive %s", fileName, arcName);
				return -1;
			}
		}
	}
	return 0;
}


unsigned int Crc32Block(block * head) {
	unsigned int crc = 0xFFFFFFFF, currentPos = 0;
	block * currentBlock = head;
	while (currentBlock != NULL) {
		while (currentPos <= currentBlock->index) {
			crc = (crc >> 8) ^ Crc32Table[(crc ^ currentBlock->data[currentPos]) & 0xFF];
			currentPos++;
		}
		currentPos = 0;
		currentBlock = currentBlock->next;
	}

	return crc ^ 0xFFFFFFFF;
}


struct MinHeapNode* newNode(char data, unsigned freq)
{
	struct MinHeapNode* temp =
		(struct MinHeapNode*) malloc(sizeof(struct MinHeapNode));
	temp->left = temp->right = temp->parent = NULL;
	temp->data = data;
	temp->freq = freq;
	return temp;
}

struct MinHeap* createMinHeap(unsigned capacity)
{
	struct MinHeap* minHeap =
		(struct MinHeap*) malloc(sizeof(struct MinHeap));
	minHeap->size = 0;  // current size is 0
	minHeap->capacity = capacity;
	minHeap->array =
		(struct MinHeapNode**)malloc(minHeap->capacity * sizeof(struct MinHeapNode*));
	return minHeap;
}

void swapMinHeapNode(struct MinHeapNode** a, struct MinHeapNode** b)
{
	struct MinHeapNode* t = *a;
	*a = *b;
	*b = t;
}

void minHeapify(struct MinHeap* minHeap, int idx)
{
	int smallest = idx;
	unsigned int left = 2 * idx + 1;
	unsigned int right = 2 * idx + 2;

	if (left < minHeap->size &&
		minHeap->array[left]->freq < minHeap->array[smallest]->freq)
		smallest = left;

	if (right < minHeap->size &&
		minHeap->array[right]->freq < minHeap->array[smallest]->freq)
		smallest = right;

	if (smallest != idx)
	{
		swapMinHeapNode(&minHeap->array[smallest], &minHeap->array[idx]);
		minHeapify(minHeap, smallest);
	}
}

int isSizeOne(struct MinHeap* minHeap)
{
	return (minHeap->size == 1);
}

struct MinHeapNode* extractMin(struct MinHeap* minHeap)
{
	struct MinHeapNode* temp = minHeap->array[0];
	minHeap->array[0] = minHeap->array[minHeap->size - 1];
	--minHeap->size;
	minHeapify(minHeap, 0);
	return temp;
}

void insertMinHeap(struct MinHeap* minHeap, struct MinHeapNode* minHeapNode)
{
	++minHeap->size;
	int i = minHeap->size - 1;
	while (i && minHeapNode->freq < minHeap->array[(i - 1) / 2]->freq)
	{
		minHeap->array[i] = minHeap->array[(i - 1) / 2];
		i = (i - 1) / 2;
	}
	minHeap->array[i] = minHeapNode;
}

void buildMinHeap(struct MinHeap* minHeap)
{
	int n = minHeap->size - 1;
	int i;
	for (i = (n - 1) / 2; i >= 0; --i)
		minHeapify(minHeap, i);
}

void printArr(int arr[], int n)
{
	int i;
	for (i = 0; i < n; ++i)
		printf("%d", arr[i]);
	printf("\n");
}

int isLeaf(struct MinHeapNode* root)
{
	return !(root->left) && !(root->right);
}

struct MinHeap* createAndBuildMinHeap(char data[], int freq[], int size)
{
	int i;
	struct MinHeap* minHeap = createMinHeap(size);
	for (i = 0; i < size; ++i)
		minHeap->array[i] = newNode(data[i], freq[i]);
	minHeap->size = size;
	buildMinHeap(minHeap);
	return minHeap;
}
int getBit(char *source, unsigned long posChar, int posBit);

unsigned short createAlphabet(unsigned long * freq, unsigned char * alphabet) {
	int i;
	unsigned short len = 0;
	for(i=0; i<256; i++)
		if (freq[i]) {
			len++;
			alphabet[len-1] = (unsigned char)i;
			freq[len - 1] = freq[i];

		}
	return len;
}

// The main function that builds Huffman tree
struct MinHeapNode* buildHuffmanTree(char data[], int freq[], unsigned short size)
{
	struct MinHeapNode *left, *right, *top;

	// Step 1: Create a min heap of capacity equal to size.  Initially, there are
	// modes equal to size.
	struct MinHeap* minHeap = createAndBuildMinHeap(data, freq, size);

	// Iterate while size of heap doesn't become 1
	while (!isSizeOne(minHeap))
	{
		// Step 2: Extract the two minimum freq items from min heap
		left = extractMin(minHeap);
		right = extractMin(minHeap);

		// Step 3:  Create a new internal node with frequency equal to the
		// sum of the two nodes frequencies. Make the two extracted node as
		// left and right children of this new node. Add this node to the min heap
		// '$' is a special value for internal nodes, not used
		top = newNode('$', left->freq + right->freq);
		left->parent = top;
		top->left = left;
		right->parent = top;		
		top->right = right;
		insertMinHeap(minHeap, top);
	}
	// Step 4: The remaining node is the root node and the tree is complete.
	return extractMin(minHeap);
}

typedef struct {
	unsigned char symbol;
	unsigned char code;
	unsigned char len;
}CharCode;


void treeToArr(struct MinHeapNode* node, huffCode *ht) {
	int arr[256];
	int len = 0, i;
	int currentBranch = 0;// 0 - left, 1 - right
	int direction = 0; // 0 - down, 1 -Up 
	struct MinHeapNode* lastNode = NULL;
	while (node != NULL)
	{
		if (lastNode == node->parent)
		{
			if (node->left != NULL)
			{
				lastNode = node;
				node = node->left;
				arr[len] = 0;
				len++;
				continue;
			}
			else
				lastNode = NULL;
		}
		if (lastNode == node->left)
		{
			//Output(node);
			if ((node->left==NULL)&&(node->right==NULL)){
				for (i = 0; i < len; i++) ht[node->data].code[i] = arr[i];
				ht[node->data].len = len;
			}
			if (node->right != NULL)
			{
				lastNode = node;
				node = node->right;
				arr[len] = 1;
				len++;
				continue;
			}
			else
				lastNode = NULL;
		}
		if (lastNode == node->right)
		{
			lastNode = node;
			node = node->parent;
			len--;
		}
	}
	return;
}

void setBitToZero(unsigned char *symbol, unsigned char position){
	unsigned char mask;
	mask=~(1<<(7-position));
	*symbol=(*symbol) & mask;
}

block * addByteToBlock(block * head, unsigned char val, unsigned char remainder) {
	block * currentBlock = head;
	block * temp;
	while (currentBlock->next) currentBlock = currentBlock->next;
	currentBlock->data[currentBlock->index] = val;
	if (remainder) currentBlock->remainder = remainder;
	currentBlock->index++;
	if (currentBlock->index > 65534) {
		temp = (block *)malloc(sizeof(block));
		memset(temp->data, 0, 65536);
		temp->index = 0;
		temp->remainder = 0;
		temp->next = NULL;
		currentBlock->next = temp;
	}
	return currentBlock;
}

void huffEncode(huffCode ht[], unsigned char *source, unsigned long sourceLen, unsigned long *destLen, unsigned char *remainder){
	unsigned long i,j;
	unsigned char currentChar=255, currentPosDest = 0;
	block * currentBlock;
//	unsigned char mask;
	int currentPosSource=0;
	i=0;
	j=0;
	*remainder = 0;
	currentBlock = headCodedBlock;
	//printProgress(0,100);
	while(i<sourceLen){
		if (ht[source[i]].code[currentPosSource]==0) 
			setBitToZero(&currentChar,currentPosDest);
		currentPosDest++;
		if(currentPosDest==8){
			j++;
			currentBlock = addByteToBlock(currentBlock, currentChar, 0);
			currentChar=255;
			currentPosDest=0;
		}
		currentPosSource++;
		if(currentPosSource==ht[source[i]].len){
			i++;
			if (i % 10000 == 0) printProgress(i, sourceLen);
			currentPosSource=0;
		}
	}
	if (currentPosDest > 0) {
		j++;
		currentBlock = addByteToBlock(currentBlock, currentChar, currentPosDest);		
	}
	*destLen = j;
	*remainder = currentPosDest;
	//printProgress(sourceLen, sourceLen);
}

void huffDecode(struct MinHeapNode* root, unsigned char *source, unsigned long lenSource, unsigned char remainder, int *lenDest){
	unsigned long currentCharPos=0, tempLenDest=0;
	int currentBitPos = 0, currentBitValue = 0;
	unsigned char bitLimit = 7;
	struct MinHeapNode* currentNode = root;
	block * currentBlock;
	currentBlock = headCodedBlock;
	while(currentCharPos<lenSource){
		if (getBit(source, currentCharPos, currentBitPos)==1) currentNode=currentNode->right;
		else currentNode=currentNode->left;
		if(currentNode->freq==1){
			tempLenDest++;
			currentBlock = addByteToBlock(currentBlock, currentNode->data, 0);
			currentNode=root;
		} 
		currentBitPos++;
		if(currentBitPos>bitLimit){
			currentBitPos=0;
			currentCharPos++;
		}
		if((currentCharPos==(lenSource-1)) && (remainder!=0)){
			bitLimit=remainder;
		}
	}
	*lenDest=tempLenDest;	
}

 void encodeTreeToStr(struct MinHeapNode* root, unsigned char * alphabet, int * lenTree, unsigned short * lenAlph) {
	struct MinHeapNode* lastNode = NULL;
	struct MinHeapNode* node = root;
 	if (*lenTree==0) codedTree = (char*)malloc(1);
 	if (isLeaf(root)) {
 		*lenAlph = *lenAlph + 1;
 		alphabet[*lenAlph - 1] = root->data;
 	}
 	else {
		*lenAlph = 0;

		while ( node!= NULL)
		{
			if (lastNode == node->parent)
			{
				if (node->left != NULL)
				{
					lastNode = node;
					node = node->left;
					*lenTree = *lenTree + 1;
					codedTree = (unsigned char *)realloc(codedTree, *lenTree);
					codedTree[*lenTree - 1] = '0';
					continue;
				}
				else
					lastNode = NULL;
			}
			if (lastNode == node->left)
			{
				//Output(node);
				if ((node->left == NULL) && (node->right == NULL)) {
					*lenAlph = *lenAlph + 1;
					alphabet[*lenAlph - 1] = node->data;
				}
				if (node->right != NULL)
				{
					lastNode = node;
					node = node->right;
					*lenTree = *lenTree + 1;
					codedTree = (unsigned char *)realloc(codedTree, *lenTree);
					codedTree[*lenTree - 1] = '0';
					continue;
				}
				else
					lastNode = NULL;
			}
			if (lastNode == node->right)
			{
				lastNode = node;
				node = node->parent;
				*lenTree = *lenTree + 1;
				codedTree = (unsigned char *)realloc(codedTree, *lenTree);
				codedTree[*lenTree - 1] = '1';
			}
		}
 	}
 }

void treeEncode(struct MinHeapNode* root, unsigned char *alphabet, unsigned char *alphLen, int *lenCodedTree, unsigned char *remainder){
	int currentBranch=0;// 0 - left, 1 - right
	int direction = 0; // 0 - down, 1 -Up 
	*alphLen = 0;
	alphabet = (unsigned char *)malloc(0);
	unsigned char currentBitPos = 0;
	struct MinHeapNode* currentNode = root;
	unsigned char currentTreeByte=255;	
	while((currentBranch!=1) || (currentNode != root)){		
		if (direction==0){
			setBitToZero(&currentTreeByte,currentBitPos);
			if (root->left) {
				root = root->left;
				currentBranch=0;		
			}
			else {
				if(root->right) {
					root = root->right;
					currentBranch=1;
				}
			}
		}
		else {
			root=root->parent;	
			if (root->right){
				direction=0;
				setBitToZero(&currentTreeByte,currentBitPos);
				root = root->right;
				currentBranch = 1;
			}
		}
		currentBitPos++;
		if(currentBitPos>7){
			currentBitPos=0;
			*lenCodedTree = *lenCodedTree + 1;
			codedTree = realloc(codedTree,*lenCodedTree);
			codedTree[*lenCodedTree-1]=currentTreeByte;
			currentTreeByte =255;
		}
		if(isLeaf(root)){
			*alphLen = *alphLen + 1;
			alphabet = realloc(alphabet,*alphLen);
			alphabet[*alphLen-1]=root->data;
			direction = 1;	
		}
	}	
		*lenCodedTree++;
		codedTree = realloc(codedTree,*lenCodedTree);
		codedTree[*lenCodedTree-1]=currentTreeByte;
		*remainder=currentBitPos;
		return;
}

void encodeTreeStrToByte(unsigned char * source, int lenSource, unsigned char * dest, int * lenDest, unsigned char * remainder) {
	unsigned char currentBitPos = 0, currentByte = 255;
	int i;
	*lenDest = 0;
	for (i = 0; i < lenSource; i++) {
		if (source[i] == '0') setBitToZero(&currentByte, currentBitPos);
		currentBitPos++;
		if (currentBitPos > 7) {
			*lenDest = *lenDest + 1;
			dest = (unsigned char*)realloc(dest, *lenDest);
			dest[*lenDest - 1] = currentByte;
			currentBitPos = 0;
			currentByte = 255;
		}
	}
	if (currentBitPos > 0) {
		*lenDest = *lenDest + 1;
		dest = (unsigned char*)realloc(dest, *lenDest);
		dest[*lenDest - 1] = currentByte;
		*remainder = currentBitPos-1;
	}
}

int getBit(char *source, unsigned long posChar, int posBit) {
	unsigned char c = source[posChar];
	unsigned char mask;
	mask = 1 << (7 - posBit);
	if ((c & mask) == 0) return 0;
	else return 1;
}

void treeDecode(struct MinHeapNode* root, char *codedTree, int lenCodedTree, /*unsigned char remainder,*/ char *alphabet, int alphLen) {

	int direction, currentIndex = 0, currentPos = 0, currentBit = 0, prevDirection = getBit(codedTree,currentPos, currentBit);
	struct MinHeapNode *currentNode = root;
	struct MinHeapNode* temp =
		(struct MinHeapNode*) malloc(sizeof(struct MinHeapNode));
	temp->parent = currentNode;
	currentNode->left = temp;
	currentNode = currentNode->left;
	while (currentIndex < alphLen) {
		currentBit++;
		if (currentBit == 8) {
			currentBit = 0;
			currentPos++;
		}
		direction = getBit(codedTree,currentPos, currentBit);
		if ((prevDirection == 0) && (direction == 0)) {
			temp =(struct MinHeapNode*) malloc(sizeof(struct MinHeapNode));
			temp->parent = currentNode;
			temp->freq = 0;
			currentNode->left = temp;
			currentNode = currentNode->left;
		}
		if ((prevDirection == 0) && (direction == 1)) {
			currentNode->data = alphabet[currentIndex];
			temp->freq = 1;
			currentIndex++;
			currentNode = currentNode->parent;
		}
		if ((prevDirection == 1) && (direction == 0)) {
			temp = (struct MinHeapNode*) malloc(sizeof(struct MinHeapNode));
			temp->parent = currentNode;
			temp->freq = 0;
			currentNode->right = temp;
			currentNode = currentNode->right;
		}
		if ((prevDirection == 1) && (direction == 1)) {
			currentNode = currentNode->parent;
		}
		prevDirection = direction;

	}
	return;
}

void write_default_header(FILE *f)
{
	char block[7];
	block[0] = (char)0x48;
	block[1] = (char)0x55;
	block[2] = (char)0x46;
	block[3] = (char)0x21;
	block[4] = (char)0xBB;
	block[5] = (char)0x48;
	block[6] = '\0';
	fwrite(block, 1, 6, f);
}

void countChars(unsigned char *source, unsigned long len, unsigned long *amount)
{
	unsigned int i;
	for (i = 0; i < 256; i++) amount[i] = 0;
	for (i = 0; i < len; i++) 
		amount[source[i]]++;
}


void write_file_header(FILE *arc, char *file_name)//not compressed
{
	head_file hf;
	int res;
		//, i;
	unsigned long sLen;
	unsigned char *str;
	char file_header[40];
//	errno_t err;
	FILE *src;
	hf.head_type = 0x63;
	hf.head_flags = 0x2;
	struct _stat buf;
	res = _stat(file_name, &buf);
	if (res != 0)
	{
		perror("Problem getting information");
		switch (errno)
		{
		case ENOENT:
			printf("File %s not found.\n", file_name);
			break;
		case EINVAL:
			printf("Invalid parameter to _stat.\n");
			break;
		default:
			/* Should never be reached. */
			printf("Unexpected error in _stat.\n");
		}
	}
	else
	{
		hf.pack_size = buf.st_size;
		hf.unpack_size = buf.st_size;
		hf.f_time = (unsigned int)buf.st_ctime;
		res = fopen_s(&src, file_name, "rb");
		if (res!=0)
		{
			printf("\nFile open error\n");
		}
		else
		{
			sLen = buf.st_size;
			if (!(str = (char *)malloc((sLen + 1) * sizeof(char))))
				printf("\nAllocation memory error\n");
			else
			{
				fread(str, sizeof(char), sLen, src);
				str[sLen] = '\0';
			}
			fclose(src);
			if (str)
			{
				hf.file_crc = Crc32(str, sLen); 
				file_name = deletePath(file_name);
				hf.name_size = (unsigned short)strlen(file_name);
				hf.attrib = buf.st_mode;
				hf.head_size = 27;
				file_header[0] = hf.head_type;
				file_header[1] = hf.head_flags;
				file_header[2] = hf.head_size;
				file_header[3] = (hf.pack_size & 0xFF000000) >> 24;
				file_header[4] = ((hf.pack_size & 0x00FF0000) >> 16);
				file_header[5] = ((hf.pack_size & 0x0000FF00) >> 8);
				file_header[6] = ((hf.pack_size & 0x000000FF));
				file_header[7] = ((hf.unpack_size & 0xFF000000) >> 24);
				file_header[8] = ((hf.unpack_size & 0x00FF0000) >> 16);
				file_header[9] = ((hf.unpack_size & 0x0000FF00) >> 8);
				file_header[10] = ((hf.unpack_size & 0x000000FF));
				file_header[11] = ((hf.name_size & 0xFF00)>>8);
				file_header[12] = ((hf.name_size & 0x00FF));
				file_header[13] = ((hf.file_crc & 0xFF000000) >> 24);
				file_header[14] = ((hf.file_crc & 0x00FF0000) >> 16);
				file_header[15] = ((hf.file_crc & 0x0000FF00) >> 8);
				file_header[16] = ((hf.file_crc & 0x000000FF));
				file_header[17] = ((hf.f_time & 0xFF000000) >> 24);
				file_header[18] = ((hf.f_time & 0x00FF0000) >> 16);
				file_header[19] = ((hf.f_time & 0x0000FF00) >> 8);
				file_header[20] = ((hf.f_time & 0x000000FF));
				file_header[21] = ((hf.attrib & 0xFF000000) >> 24);
				file_header[22] = ((hf.attrib & 0x00FF0000) >> 16);
				file_header[23] = ((hf.attrib & 0x0000FF00) >> 8);
				file_header[24] = ((hf.attrib & 0x000000FF));
				hf.head_crc = (Crc32(file_header, 25) & 0x0000FFFF);
				file_header[25] = ((hf.head_crc & 0xFF00) >> 8);
				file_header[26] = ((hf.head_crc & 0x00FF));			
				fwrite(file_header, 1, 28, arc);
				fwrite(file_name, 1, hf.name_size, arc);
				fwrite(str, 1, hf.pack_size, arc);
			}
		}
	}
}
//zero file
void writeZeroBlock(FILE * arc, char * fileName) {
	head_file hf;
	int res;
	unsigned short crc;
	unsigned char fileHeader[14];
	struct _stat buf;
	res = _stat(fileName, &buf);
	if (res != 0)
	{
		perror("\nProblem getting information\n");
		switch (errno)
		{
		case ENOENT:
			printf("\nFile %s not found.\n", fileName);
			break;
		case EINVAL:
			printf("\nInvalid parameter to _stat.\n");
			break;
		default:
			/* Should never be reached. */
			printf("Unexpected error in _stat.\n");
		}
	}
	hf.head_type = 0x60;
	hf.f_time = (unsigned int)buf.st_ctime;
	fileName = deletePath(fileName);
	hf.name_size = (unsigned short)strlen(fileName);
	fileHeader[0] = hf.head_type;//type
	fileHeader[1] = (hf.f_time & 0xFF000000) >> 24;
	fileHeader[2] = ((hf.f_time & 0x00FF0000) >> 16);
	fileHeader[3] = ((hf.f_time & 0x0000FF00) >> 8);
	fileHeader[4] = ((hf.f_time & 0x000000FF));
	fileHeader[5] = ((hf.name_size & 0xFF00) >> 8);
	fileHeader[6] = ((hf.name_size & 0x00FF));
	crc = (Crc32(fileName, hf.name_size) & 0x0000FFFF);
	fileHeader[7] = ((crc & 0xFF00) >> 8); // crcName
	fileHeader[8] = ((crc & 0x00FF));
	crc = (Crc32(fileHeader, 9) & 0x0000FFFF);
	fileHeader[9] = ((crc & 0xFF00) >> 8);
	fileHeader[10] = ((crc & 0x00FF));
	fwrite(fileHeader, 1, 11, arc);
	fwrite(fileName, 1, hf.name_size, arc);
	return;
}
//file contain repeated single symbol 
void writeOneBlock(FILE * arc, char * fileName, unsigned char symbol) {
	head_file hf;
	int res;
	unsigned char fileHeader[19];
	struct _stat buf;
	unsigned short crc;
	res = _stat(fileName, &buf);
	if (res != 0)
	{
		perror("Problem getting information");
		switch (errno)
		{
		case ENOENT:
			printf("File %s not found.\n", fileName);
			break;
		case EINVAL:
			printf("Invalid parameter to _stat.\n");
			break;
		default:
			/* Should never be reached. */
			printf("Unexpected error in _stat.\n");
		}
	}
	hf.head_type = 0x61;
	hf.f_time = (unsigned int)buf.st_ctime;
	fileName = deletePath(fileName);
	hf.name_size = (unsigned short)strlen(fileName);
	hf.unpack_size = buf.st_size;
	fileHeader[0] = hf.head_type;//type
	fileHeader[1] = symbol;
	fileHeader[2] = (hf.unpack_size & 0xFF000000) >> 24;
	fileHeader[3] = ((hf.unpack_size & 0x00FF0000) >> 16);
	fileHeader[4] = ((hf.unpack_size & 0x0000FF00) >> 8);
	fileHeader[5] = ((hf.unpack_size & 0x000000FF));
	fileHeader[6] = (hf.f_time & 0xFF000000) >> 24;
	fileHeader[7] = ((hf.f_time & 0x00FF0000) >> 16);
	fileHeader[8] = ((hf.f_time & 0x0000FF00) >> 8);
	fileHeader[9] = ((hf.f_time & 0x000000FF));
	fileHeader[10] = ((hf.name_size & 0xFF00) >> 8);
	fileHeader[11] = ((hf.name_size & 0x00FF));
	crc = (Crc32(fileName, hf.name_size) & 0x0000FFFF);
	fileHeader[12] = ((crc & 0xFF00) >> 8); // crcName
	fileHeader[13] = ((crc & 0x00FF));
	crc = (Crc32(fileHeader, 14) & 0x0000FFFF);
	fileHeader[14] = ((crc & 0xFF00) >> 8);
	fileHeader[15] = ((crc & 0x00FF));
	fwrite(fileHeader, 1, 16, arc);
	fwrite(fileName, 1, hf.name_size, arc);
	return;
}

void freeList(block * head) {
	block * p;
	while (head) {
		p = head->next;
		free(head);
		head = p;
	}
	return;
}

void encodeAndWrite(FILE *arc, char *file_name) {
	head_file hf;
	int res, lenCodedTreeByte = 0;
	unsigned long sLen, freq[256];
	unsigned int lenCodedStr, tempCRC;
	int lenCodedTree = 0, /**arr,*/ top = 0 ;
	unsigned char *str, *codedStr, *codedTreeByte, remainderTree, remainderStr, *alphabet = (char*)malloc(256);
	unsigned short lenAlph;
	unsigned char file_header[40];
	codedStr = (char*)malloc(1);
//	errno_t err;
	FILE *src;
	huffCode *ht = (huffCode*)malloc(sizeof(huffCode)*256);
	struct MinHeapNode * root;
	hf.head_type = 0x62;
	hf.head_flags = 0x2;
	struct _stat buf;
	res = _stat(file_name, &buf);
	if (res != 0)
	{
		perror("Problem getting information");
		switch (errno)
		{
		case ENOENT:
			printf("File %s not found.\n", file_name);
			break;
		case EINVAL:
			printf("Invalid parameter to _stat.\n");
			break;
		default:
			/* Should never be reached. */
			printf("Unexpected error in _stat.\n");
		}
	}
	else
	{	
		hf.unpack_size = buf.st_size;
		hf.f_time = (unsigned int)buf.st_ctime;
		res = fopen_s(&src, file_name, "rb");
		if (res != 0)
		{
			printf("File open error");
		}
		else
		{
			sLen = buf.st_size;
			if (sLen == 0) {
				writeZeroBlock(arc, file_name);
				return;
			}
			if (!(str = (char *)malloc((sLen + 1) * sizeof(char))))
				printf("Allocation memory error\n");
			else
			{
				fread(str, sizeof(char), sLen, src);
				str[sLen] = '\0';
			}
			if (sLen<450){
				write_file_header(arc, file_name);
				return;
			}
			fclose(src);
			if (str)
			{
				headCodedBlock = (block *)malloc(sizeof(block));
				memset(headCodedBlock->data, 0, 65536);
				headCodedBlock->index = 0;
				headCodedBlock->remainder = 0;
				headCodedBlock->next = NULL;
				printf("Counting chars");
				countChars(str, sLen, freq);
				printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
				printf("Creating alphabet");
				lenAlph = createAlphabet(freq, alphabet);
				printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
				if (lenAlph == 1) {
					writeOneBlock(arc, file_name, alphabet[0]);
					return;
				}
				printf("Building Huffman tree");
				root = buildHuffmanTree(alphabet, freq, lenAlph);
				printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
				treeToArr(root, ht);
				hf.alphLen = lenAlph;
				codedStr = NULL;
				codedStr = (unsigned char*)malloc(1);
				huffEncode(ht, str, sLen, &lenCodedStr, &remainderStr);
				lenAlph = 0;
				encodeTreeToStr(root, alphabet, &lenCodedTree, &lenAlph);
				codedTreeByte = (unsigned char*)malloc(0);
				encodeTreeStrToByte(codedTree,lenCodedTree, codedTreeByte, &lenCodedTreeByte, &remainderTree);
				hf.pack_size = lenCodedStr;
				hf.remainder = remainderStr;
				hf.lenCodedTree = lenCodedTreeByte;
				hf.remainderTree = remainderTree;
				file_name = deletePath(file_name);
				hf.name_size = (unsigned short)strlen(file_name);
				hf.file_crc = 0;
				tempCRC = Crc32(str, sLen);
				hf.file_crc += tempCRC;
				tempCRC = Crc32(alphabet, lenAlph);
				hf.file_crc += tempCRC; 
				tempCRC = Crc32(codedTreeByte, lenCodedTreeByte);
				hf.file_crc += tempCRC; 
				tempCRC = Crc32(file_name, hf.name_size);
				hf.file_crc += tempCRC;
				hf.attrib = buf.st_mode;
				file_header[0] = hf.head_type;//type
				file_header[1] = hf.head_flags;
				file_header[2] = (hf.pack_size & 0xFF000000) >> 24;
				file_header[3] = ((hf.pack_size & 0x00FF0000) >> 16);
				file_header[4] = ((hf.pack_size & 0x0000FF00) >> 8);
				file_header[5] = ((hf.pack_size & 0x000000FF));
				file_header[6] = hf.remainder;
				file_header[7] = ((hf.unpack_size & 0xFF000000) >> 24);
				file_header[8] = ((hf.unpack_size & 0x00FF0000) >> 16);
				file_header[9] = ((hf.unpack_size & 0x0000FF00) >> 8);
				file_header[10] = ((hf.unpack_size & 0x000000FF));
				file_header[11] = ((hf.name_size & 0xFF00) >> 8);
				file_header[12] = ((hf.name_size & 0x00FF));
				file_header[13] = ((hf.file_crc & 0xFF000000) >> 24);
				file_header[14] = ((hf.file_crc & 0x00FF0000) >> 16);
				file_header[15] = ((hf.file_crc & 0x0000FF00) >> 8);
				file_header[16] = ((hf.file_crc & 0x000000FF));
				file_header[17] = ((hf.f_time & 0xFF000000) >> 24);
				file_header[18] = ((hf.f_time & 0x00FF0000) >> 16);
				file_header[19] = ((hf.f_time & 0x0000FF00) >> 8);
				file_header[20] = ((hf.f_time & 0x000000FF));
				file_header[21] = ((hf.attrib & 0xFF000000) >> 24);
				file_header[22] = ((hf.attrib & 0x00FF0000) >> 16);
				file_header[23] = ((hf.attrib & 0x0000FF00) >> 8);
				file_header[24] = ((hf.attrib & 0x000000FF));
				file_header[25] = ((hf.alphLen & 0xFF00) >> 8);
				file_header[26] = ((hf.alphLen & 0x00FF));
				file_header[27] = ((hf.lenCodedTree & 0xFF000000) >> 24);
				file_header[28] = ((hf.lenCodedTree & 0x00FF0000) >> 16);
				file_header[29] = ((hf.lenCodedTree & 0x0000FF00) >> 8);
				file_header[30] = ((hf.lenCodedTree & 0x000000FF));
				file_header[31] = hf.remainderTree;
				hf.head_crc = (Crc32(file_header, 32) & 0x0000FFFF);
				file_header[32] = ((hf.head_crc & 0xFF00) >> 8);
				file_header[33] = ((hf.head_crc & 0x00FF));
				printf("Write to disk");
				fwrite(file_header, 1, 34, arc);
				fwrite(file_name, 1, hf.name_size, arc);
				fwrite(alphabet, 1, hf.alphLen, arc);
				fwrite(codedTreeByte, 1, hf.lenCodedTree, arc);
				blockWrite(headCodedBlock, arc);
				freeList(headCodedBlock);
				printf("\b\b\b\b\b\b\b\b\b\b\b\b\b");
				printf("             ");
				printf("\b\b\b\b\b\b\b\b\b\b\b\b\b");
			}
		}
	}
	return;
}

int deleteFromArchive(FILE * arc, unsigned int ofset) {

	return 0;
}


size_t decodeAndWrite(FILE * f, size_t ofset) {
	head_file hf;
	FILE * tmp;
	int res;
	struct _utimbuf ut;
	unsigned char * str;
	unsigned char * alphabet;
	unsigned char * codedTreeByte;
	unsigned char * fileName; 
	unsigned char typeOfBlock[1];
	unsigned char tmpfile[L_tmpnam_s];
	unsigned short crc;
	unsigned int /*crc4,*/ unpackLen, tempCRC;
	char dir[255],curDir[255];
	errno_t err;
	struct MinHeapNode * root;
	fseek(f, ofset, SEEK_SET);	
	fread(typeOfBlock, 1, 1, f);
	tmpnam_s(tmpfile, L_tmpnam_s);
	_getcwd(curDir, 255);
	strcpy_s(dir,strlen(curDir)+1,curDir);
	switch (typeOfBlock[0])
	{ 
	case 0x60:
		if (!(str = (char *)malloc(10))){
			printf("Allocation memory error\n");
			return 0;
		}
		fread(str, 1, 10, f);
		ofset += 10;
		crc = (str[8] << 8) + str[9];
		if (crc != (Crc32(str, 8) & 0x0000FFFF)) {
			printf("Head of block corrupted\n");
			return 0;
		}
		hf.name_size = (str[4] << 8) + str[5];
		hf.f_time = (str[0] << 24) + (str[1] << 16) + (str[2] << 8) + str[3];
		fileName = (char *)malloc(hf.name_size + 1);
		fseek(f, ofset, SEEK_SET);
		fread(fileName, sizeof(char), hf.name_size, f);
		ofset+=hf.name_size;
		crc = (str[6] << 8) + str[7];
		if (crc != (Crc32(fileName, hf.name_size) & 0x0000FFFF)) {
			printf("Archive corrupted\n");
			return 0;
		}
		fileName[hf.name_size] = '\0';
		printf("\nunpacking file %s", fileName);
		ofset += hf.name_size;
		res = fopen_s(&tmp, tmpfile, "wb");
		fclose(tmp);
		strcpy_s(dir, strlen(curDir) + 1, curDir);
		strcat_s(dir, strlen(dir) + 2, "\\");
		strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
		strcpy_s(fileName, strlen(dir) + 1, dir);
		if ((_access(fileName, 0)) != -1)
		{
			while ((_access(fileName, 0)) != -1) {
				printf_s("\nFile already exist %s\n", fileName);
				printf("\nEnter new name ");
				scanf_s("%s", fileName, 255);
				strcpy_s(dir, strlen(curDir) + 1, curDir);
				strcat_s(dir, strlen(dir) + 2, "\\");
				strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
				strcpy_s(fileName, strlen(dir) + 1, dir);
			}
			if (copyFiles(tmpfile, 0, fileName) == 0) {
				remove(tmpfile);
				printf(" -OK\n");
			}
			/*ut.actime = hf.f_time;
			ut.modtime = hf.f_time;
			if (_utime(fileName, &ut) == -1){
			perror("\n_utime failed\n");
			return 0;
			}*/
		}
		else
		{
			if (copyFiles(tmpfile, 0, fileName) == 0) {
				remove(tmpfile);
				printf(" -OK\n");
			}
			/*ut.actime = hf.f_time;
			ut.modtime = hf.f_time;
			if (_utime(fileName, &ut) == -1){
			perror("\n_utime failed\n");
			return 0;
			}*/
			printf(" -OK\n");
		}
		ut.actime = hf.f_time;
		ut.modtime = hf.f_time;
		if (_utime(fileName, &ut) == -1){
			perror("\n_utime failed\n");
			return 0;
		}
		printf(" -OK\n");
		break;
	case 0x61:
		if (!(str = (char *)malloc(15))){
			printf("\nAllocation memory error\n");
			return 0;
		}
		fread(str, 1, 15, f);
		ofset += 15;
		crc = (str[14] << 8) + str[15];
		if (crc != (Crc32(str, 14) & 0x0000FFFF)) {
			printf("\nHead of block corrupted\n");
			return 0;
		}
		hf.name_size = (str[9] << 8) + str[10];
		hf.f_time = (str[5] << 24) + (str[6] << 16) + (str[7] << 8) + str[8];
		fileName = (char *)malloc(hf.name_size + 1);
		printf("\nunpacking %s", fileName);
		fseek(f, ofset, SEEK_SET);
		fread(fileName, sizeof(char), hf.name_size, f);
		ofset+=hf.name_size;
		crc = (str[11] << 8) + str[12];
		if (crc != (Crc32(fileName, hf.name_size) & 0x0000FFFF)) {
			printf("Archive corrupted\n");
			return 0;
		}
		hf.unpack_size = (str[1] << 24) + (str[2] << 16) + (str[3] << 8) + str[4];
		if (!(codedStr = (char *)malloc(hf.unpack_size))){
			printf("Allocation memory error\n");
			return 0;
		}
		else
		{
			memset(codedStr, str[0], hf.unpack_size);
			fileName[hf.name_size] = '\0';
			printf("unpacking %s", fileName);
			//tmpnam_s(tmpfile, L_tmpnam_s);
			res = fopen_s(&tmp, tmpfile, "wb");
			fwrite(codedStr, 1 , hf.unpack_size, tmp);
			fclose(tmp);
			strcpy_s(dir, strlen(curDir) + 1, curDir);
			strcat_s(dir, strlen(dir) + 2, "\\");
			strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
			strcpy_s(fileName, strlen(dir) + 1, dir);
			if ((_access(fileName, 0)) != -1)
			{
				while ((_access(fileName, 0)) != -1) {
					printf_s("\nFile already exist %s\n", fileName);
					printf("\nEnter new name ");
					scanf_s("%s", fileName, 255);
					strcpy_s(dir, strlen(curDir) + 1, curDir);
					strcat_s(dir, strlen(dir) + 2, "\\");
					strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
					strcpy_s(fileName, strlen(dir) + 1, dir);
				}
				if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
					remove(tmpfile);
					printf(" -OK\n");
				}
				/*ut.actime = hf.f_time;
				ut.modtime = hf.f_time;
				if (_utime(fileName, &ut) == -1){
				perror("\n_utime failed\n");
				return 0;
				}*/
			}
			else
			{
				if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
					remove(tmpfile);
					printf(" -OK\n");
				}
				/*ut.actime = hf.f_time;
				ut.modtime = hf.f_time;
				if (_utime(fileName, &ut) == -1){
				perror("\n_utime failed\n");
				return 0;
				}*/
				printf(" -OK\n");
			}
			printf(" -OK\n");
		}
		break;
	case 0x62: //decode compressed block
		if (!(str = (char *)malloc(34))){
			printf("\nAllocation memory error\n");
			return 0;
		}
		fseek(f, ofset, SEEK_SET);
		fread(str, 1, 34, f);
		ofset += 34;
		crc = (str[32] << 8) + str[33];
		if (crc != (Crc32(str, 32) & 0x0000FFFF)) {
			printf("\nHead of block corrupted\n");
			return 0;
		}
		hf.pack_size = (str[2] << 24) + (str[3] << 16) + (str[4] << 8) + str[5];
		hf.remainder = str[6];
		hf.unpack_size = (str[7] << 24) + (str[8] << 16) + (str[9] << 8) + str[10];
		hf.name_size = (str[11] << 8) + str[12];
		hf.file_crc = (str[13] << 24) + (str[14] << 16) + (str[15] << 8) + str[16];
		hf.f_time = (str[17] << 24) + (str[18] << 16) + (str[19] << 8) + str[20];
		hf.attrib = (str[21] << 24) + (str[22] << 16) + (str[23] << 8) + str[24];
		hf.alphLen = (str[25] << 8) + str[26];
		hf.lenCodedTree = (str[27] << 24) + (str[28] << 16) + (str[29] << 8) + str[30];
		fileName = (char *)malloc(hf.name_size + 1);
		fread(fileName, 1, hf.name_size, f);
		fileName[hf.name_size]='\0';
		printf("\nunpacking file %s", fileName);
		ofset+=hf.name_size;
		alphabet = (char *)malloc(hf.alphLen + 1);
		fread(alphabet, 1, hf.alphLen, f);
		ofset+=hf.alphLen;
		codedTreeByte = (char *)malloc(hf.lenCodedTree + 1);
		fread(codedTreeByte, 1, hf.lenCodedTree, f);
		ofset+=hf.lenCodedTree;
		if (!(str = (char *)malloc(hf.pack_size+1))){
			printf("\nAllocation memory error\n");
			return 0;
		}
		fread(str, 1, hf.pack_size, f);
		ofset+=hf.pack_size;
		headCodedBlock = (block *)malloc(sizeof(block));
		memset(headCodedBlock->data, 0, 65536);
		headCodedBlock->index = 0;
		headCodedBlock->remainder = 0;
		headCodedBlock->next = NULL;
		root =(struct MinHeapNode*) malloc(sizeof(struct MinHeapNode));
		root->left = root->right = root->parent = NULL;
		root->data = '$';
		root->freq = 0;
		treeDecode(root, codedTreeByte, hf.lenCodedTree, alphabet, hf.alphLen);
		huffDecode(root,str,hf.pack_size,hf.remainder, &unpackLen);
		//free(tmpfile);
		/*err = tmpnam_s(tmpfile, L_tmpnam_s);
		if (err)
		{
			printf("Error occurred creating unique filename.\n");
			exit(1);
		}
		else
		{
			printf("%s is safe to use as a temporary file.\n", tmpfile);
		}
		tmpnam_s(tmpfile, L_tmpnam_s);*/
		res = fopen_s(&tmp, tmpfile, "wb");
		blockWrite(headCodedBlock,tmp);
		fclose(tmp);
		res = fopen_s(&tmp, tmpfile, "rb");
		tempCRC = 0;
		if (!(str = (char *)malloc((hf.unpack_size + 1) * sizeof(char))))
			printf("Allocation memory error\n");
		else
		{
			fread(str, 1, hf.unpack_size, tmp);
			str[hf.unpack_size] = '\0';
		}
		fclose(tmp);
		tempCRC += Crc32(str, hf.unpack_size);
		freeList(headCodedBlock);
		tempCRC += Crc32(alphabet, hf.alphLen);
		tempCRC += Crc32(codedTreeByte, hf.lenCodedTree);
		tempCRC += Crc32(fileName, hf.name_size);
		/*fclose(tmp);*/
		if (tempCRC != hf.file_crc) {
			printf("\nArchive corrupted");
			return 0;
		}
		strcpy_s(dir, strlen(curDir) + 1, curDir);
		strcat_s(dir, strlen(dir)+2, "\\");
		strcat_s(dir, strlen(fileName) + strlen(dir)+ 1, fileName);
		strcpy_s(fileName, strlen(dir) + 1, dir);
		if ((_access(fileName, 0)) != -1)
		{
			while ((_access(fileName, 0)) != -1) {
				printf_s("\nFile already exist %s\n", fileName);
				printf("\nEnter new name ");
				scanf_s("%s", fileName,255);
				strcpy_s(dir, strlen(curDir) + 1, curDir);
				strcat_s(dir, strlen(dir) + 2, "\\");
				strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
				strcpy_s(fileName, strlen(dir) + 1, dir);
			}
			if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
				remove(tmpfile);
				printf(" -OK\n");
			}			
			/*ut.actime = hf.f_time;
			ut.modtime = hf.f_time;
			if (_utime(fileName, &ut) == -1){
			perror("\n_utime failed\n");
			return 0;
			}*/
		}
		else
		{
			if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
				remove(tmpfile);
				printf(" -OK\n");
			}
			/*ut.actime = hf.f_time;
			ut.modtime = hf.f_time;
			if (_utime(fileName, &ut) == -1){
			perror("\n_utime failed\n");
			return 0;
			}*/
			printf(" -OK\n");
		}
		break;
	case 0x63:
		fseek(f, ofset, SEEK_SET);
		if (!(str = (char *)malloc(28))){
			printf("\nAllocation memory error\n");
			return 0;
		}
		fread(str, sizeof(char), 27, f);
		if ((str[0] != 0x63) || (str[1] != 0x02) || (str[2] != 0x1B)) {
			printf("\nArchive corrupted\n");
			return 0;
		}
		else {
			hf.pack_size = (str[3] << 24) + (str[4] << 16) + (str[5] << 8) + str[6];
			hf.unpack_size = (str[7] << 24) + (str[8] << 16) + (str[9] << 8) + str[10];
			hf.name_size = (str[11] << 8) + str[12];
			hf.file_crc = (str[13] << 24) + (str[14] << 16) + (str[15] << 8) + str[16];
			hf.f_time = (str[17] << 24) + (str[18] << 16) + (str[19] << 8) + str[20];
			hf.attrib = (str[21] << 24) + (str[22] << 16) + (str[23] << 8) + str[24];
			hf.head_crc = (str[25] << 8) + str[26];
			if (hf.head_crc != (Crc32(str, 25) & 0x0000FFFF)) {
				printf("\nArchive corrupted\n");
				return 0;
			}
			ofset += 28;
			fileName = (char *)malloc(hf.name_size + 1);
			fseek(f, ofset, SEEK_SET);
			fread(fileName, 1, hf.name_size, f);
			fileName[hf.name_size] = '\0';
			printf("\nunpacking %s", fileName);
			ofset += hf.name_size;
			/*tmpnam_s(tmpfile, L_tmpnam_s);*/
			res = fopen_s(&tmp, tmpfile, "wb");
			if (!(str = (char *)malloc((hf.pack_size + 1) * sizeof(char))))
				printf("\nAllocation memory error\n");
			else {
				fseek(f, ofset, SEEK_SET);
				fread(str, sizeof(char), hf.pack_size, f);
				if (hf.file_crc != Crc32(str, hf.pack_size)) {
					printf("\nArchive corrupted");
				}
				else {
					fwrite(str, 1, hf.pack_size, tmp);
					fclose(tmp);
					strcpy_s(dir, strlen(curDir) + 1, curDir);
					strcat_s(dir, strlen(dir) + 2, "\\");
					strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
					strcpy_s(fileName, strlen(dir) + 1, dir);
					if ((_access(fileName, 0)) != -1)
					{
						while ((_access(fileName, 0)) != -1) {
							printf_s("\nFile already exist %s\n", fileName);
							printf("\nEnter new name ");
							scanf_s("%s", fileName, 255);
							strcpy_s(dir, strlen(curDir) + 1, curDir);
							strcat_s(dir, strlen(dir) + 2, "\\");
							strcat_s(dir, strlen(fileName) + strlen(dir) + 1, fileName);
							strcpy_s(fileName, strlen(dir) + 1, dir);
						}
						if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
							remove(tmpfile);
							printf(" -OK\n");
						}
						/*ut.actime = hf.f_time;
						ut.modtime = hf.f_time;
						if (_utime(fileName, &ut) == -1){
						perror("\n_utime failed\n");
						return 0;
						}*/
					}
					else
					{
						if (copyFiles(tmpfile, hf.unpack_size, fileName) == 0) {
							remove(tmpfile);
							printf(" -OK\n");
						}
						/*ut.actime = hf.f_time;
						ut.modtime = hf.f_time;
						if (_utime(fileName, &ut) == -1){
						perror("\n_utime failed\n");
						return 0;
						}*/
						printf(" -OK\n");
					}
				}
			}
			ofset += hf.pack_size;
		}
		break;
	default:
		break;
	}
	return ofset;
}

void main(int argc, char *argv[])
{
	FILE *arc;
	//head_file hf;
	filesListNode * headFileList;
	filesListNode * currFileInList;
	char *arc_name;
	//unsigned char *str;
	//char *filename;
	int i, res_arc, res;
	size_t /*sz, */ofset;
	struct tm newtime = {0};
	headFileList = (filesListNode *) malloc(sizeof(filesListNode));
	currFileInList = (filesListNode *) malloc(sizeof(filesListNode));
	switch (argc)
	{
		case 1: 
			printf("\nHuff. The Huffman archiever, version 0.0.1 x86");
			printf("\nCopyright (c) 2017 Artyom Grigorovich, 2017 NSU");
			printf("\nUsage: huff.exe -a|-x|-l|-h [archive name] [File1 File2 ...]");
			printf("\nSamples:");
			printf("\nExtract files from archive huff.exe -x c:\\arhiv4.huf");
			printf("\nAdd file to archive        huff.exe -a arhiv4.huf d:\\temp\\input.txt");
			printf("\nList files in archive      huff.exe -l c:\\arhiv4.huf");
			printf("\nThis text                  huff.exe -h");
			break;
		case 2: 
			if ((strcmp(argv[1], "-h") == 0) || (strcmp(argv[1], "-H") == 0)) {
				printf("\nHuff. The Huffman archiever, version 0.0.1 x86");
				printf("\nCopyright (c) 2017 Artyom Grigorovich, 2017 NSU");
				printf("\nUsage: huff.exe -a|-x|-l|-h [archive name] [File1 File2 ...]");
				printf("\nSamples:");
				printf("\nExtract files from archive huff.exe -x c:\\arhiv4.huf");
				printf("\nAdd file to archive        huff.exe -a arhiv4.huf d:\\temp\\input.txt");
				printf("\nList files in archive      huff.exe -l c:\\arhiv4.huf");
				printf("\nThis text                  huff.exe -h");
			}
			break;
		case 3: 
			if ((strcmp(argv[1], "-x") == 0) || (strcmp(argv[1], "-X") == 0)) {
				res= fopen_s(&arc, argv[2], "rb");			
				if (res == 0)
				{
					headFileList = readListFiles(arc);
					while(headFileList!=NULL){
						ofset=decodeAndWrite(arc, headFileList->ofset);
						if (ofset==0) return;
						headFileList = headFileList->next;
					}
					fclose(arc);
				}
				else
				{
					printf("Error! Archive was not opened\n");
				}
			}
			if ((strcmp(argv[1], "-l") == 0) || (strcmp(argv[1], "-L") == 0)) {
				printf("Huffman archiver v 0.1\n");
				printf("Archive: %s\n", argv[2]);
				printf("List of files in archive\n");
				printf("------------------------\n");
				res = fopen_s(&arc, argv[2], "rb");
				if (res == 0)
				{
					headFileList = readListFiles(arc);
					while(headFileList!=NULL){
						printf("%s\n",headFileList->fileName);
						headFileList = headFileList->next;
					}
					fclose(arc);
				}
				else
				{
					printf("Error! Archive was not opened\n");
				}
			}
			break;
		default:
			if (argc >= 4) {
				if ((strcmp(argv[1], "-a") == 0) || (strcmp(argv[1], "-A") == 0)) {
					arc_name = (char *)malloc(strlen(argv[2]) + 2);
					res_arc = fopen_s(&arc, argv[2], "wb");
					if (res_arc == 0)
					{
						write_default_header(arc);
						for (i = 3; i < argc; i++)
						{
							printf("Huffing file %s\n", argv[i]);
							encodeAndWrite(arc, argv[i]);
							printf(" -OK\n");
						}
						fclose(arc);
						return;
					}
					else
					{
						printf("Error! Archive was not opened\n");
						return;
					}					
				}
				if ((strcmp(argv[1], "-x") == 0) || (strcmp(argv[1], "-X") == 0)) {
					res = fopen_s(&arc, argv[2], "rb");
					if (res == 0){
						headFileList = readListFiles(arc);
						for (i = 3; i < argc; i++)
						{
							printf("Exhuffing file %s\n", argv[i]);
							currFileInList = headFileList;
							ofset=0;
							while(currFileInList){
								if (strcmp(argv[i],currFileInList->fileName)==0){
									ofset = decodeAndWrite(arc, currFileInList->ofset);
									if(ofset==0) return;
									else printf(" -OK\n");
									break;
								}
								currFileInList = currFileInList->next;
							}
							if(ofset==0){
								printf("File %s not found in archive\n", argv[i]);
							}
						}
						return;
					}
					else
					{
						printf("Error! Archive was not opened\n");
						return;
					}	
				}
				if ((strcmp(argv[1], "-d") == 0) || (strcmp(argv[1], "-D") == 0)) {
					for (i = 3; i < argc; i++) {
						printf("Deleting file %s from %s", argv[i], argv[2]);
						if (deleteFileFromArchive(argv[2], argv[i]) == 0)
							printf(" - OK\n");
						else
							printf(" - Error deleting file\n");
					}
				}
			}
	}
	//getchar();
	return;
}
