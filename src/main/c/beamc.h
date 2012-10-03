#ifndef _BEAMC_H
#define _BEAMC_H 

#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "org_esa_beam_extapi_CApi.h"

/*
 * TBD:
 * 
 * Use JNI types in C-interface?
 *     Yes, because BEAM API is Java and as such it is much less verbous and more concise to reuse JNI types, and is aloso less work
 *     No, because the C-API shall be independent of its implementation or origin
 *
 * Dublicate Java API (or parts) 1:1?  (e.g. any method in Java gets its C counterpart)
 *     Yes, because the Java API docs can be reused for C API.
 *     No, because a more concise C-API can be generated. And no, because changes in the Java API need to be reflected in the C API 
 *      which will introduce a lot of work.
 * 
 * Shall C API functions return string buffers that users have to release later?
 *     Yes, otherwise the signature of Java counterparts is will be different, because by-reference arguments passing is required then.
 *          E.g. instead of
 *               char* name = get_name(obj);
 *               ...
 *               free(name);
 *          we have
 *               char name[81];
 *               get_name(obj, name, 80);
 *     No, because it is abvious that strings need to be freed on the users side.
 *
 * Shall the API allow for modification of single structures elements that are passed as arguments by-reference.?
 */

#define _BEAM_DEBUG 1

#ifdef _BEAM_DEBUG
#define BEAM_TRACE printf
#else
#define BEAM_TRACE //
#endif

typedef jobject Product;
typedef jobject Band;
typedef jobject Operator;

struct _OperatorInfo;
typedef struct _OperatorInfo OperatorInfo;

typedef void (*TileComputer)(int op_type_id, Product product, int band_index, int tile_width, int tile_height, void* tile_data);
typedef void (*TileStackComputer)(int op_type_id, Product product, int num_bands, int tile_width, int tile_height, void** tile_data);

typedef enum _LogLevel {
	LOG_NONE =  0,
	LOG_ERROR = 10,
	LOG_WARN = 20,
	LOG_INFO = 30,
	LOG_DEBUG = 40,
} LogLevel;

typedef enum _boolean { 
	FALSE = 0, 
	TRUE = 1 
} boolean;

/* not used yet */
typedef struct _SpectralInfo {
    int band_index;
	double wavelength;
	double bandwidth;
	double solar_flux;
} SpectralInfo;

/* not used yet */
typedef enum _SampleType {
	ST_INT8,
	ST_UINT8,
	ST_INT16,
	ST_UINT16,
	ST_INT32,
	ST_UINT32,
	ST_FLOAT32,
	ST_FLOAT64,   
} SampleType;

/* not used yet */
typedef struct _BandInfo{
    jobject obj_id;
	char* name;
	SampleType data_type;
	char* description;
	char* band_math_expr;
	double wavelength;
	SpectralInfo * spectral_info;
	boolean log_scaled;
	double scaling_factor;
	double scaling_offset;
	/* todo: SampleCoding sample_coding; */
} BandInfo;

typedef enum _ParameterType {
	PT_BOOLEAN = 1,
	PT_INT,
	PT_DOUBLE,
	PT_STRING,   
} ParameterType;

typedef union _ParameterValue {
	boolean b_value;
	int i_value;
	double d_value;
	const char* s_value;   
} ParameterValue;

typedef struct _ParameterInfo {
	const char* name; 
	const char* description; 
	ParameterType type;
} ParameterInfo;

typedef struct _OperatorInfo {
	const char* name; 
    const char* description; 
	TileComputer tile_computer;
	TileStackComputer tile_stack_computer;
	int num_params;
	ParameterInfo* param_infos;
} OperatorInfo;

#define MAX_NUM_OPERATORS 256

typedef struct _Beam {
	JavaVM* jvm; 
	int operator_count;
	const OperatorInfo* operator_infos[MAX_NUM_OPERATORS];
	LogLevel log_level;
} Beam;

extern Beam beam;
extern JNIEnv* jenv;

/*
 * Global functions are prefixed by 'beam'. 
 */

jboolean beam_is_jvm_created();
jboolean beam_create_jvm(const char* option_strings[], int option_count); 
jboolean beam_create_jvm_with_defaults(); 
jboolean beam_destroy_jvm(); 

jint beam_register_operator(const OperatorInfo* operator_info);
Product beam_read_product(const char* file_path);
Product beam_create_product(const char* op_name, const char** parameters, Product source_product, ...);
void beam_release_object(jobject* object);

/*
 * Product functions are prefixed by 'product'. 
 */

char* product_get_name(Product product);
int product_get_num_bands(Product product);
char** product_get_band_names(Product product);
Band product_get_band(Product product, int index);

#endif /* _BEAMC_H */ 