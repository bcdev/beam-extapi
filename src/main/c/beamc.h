#ifndef _BEAMC_H
#define _BEAMC_H 

#include <jni.h>
#include <stdlib.h>

typedef unsigned long long ObjectId;

struct StringRef {
	jstring jstr;
	const char* chars;
};

typedef StringRef* StringId;

ObjectId beamc_read_product(const char* file_path);

ObjectId beamc_create_product(const char* op_name, const char** parameters, ObjectId source_product, ...);

#endif /* _BEAMC_H */ 