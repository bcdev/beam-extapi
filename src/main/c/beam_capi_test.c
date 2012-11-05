
#include <stdio.h>
#include <stdlib.h>
#include "gen/beam_capi.h"


#define CONFIRM(S)  {printf("%s, Press enter!", S);getc(stdin);}
#define PRESS_ENTER()  {printf("Press enter!");getc(stdin);}


int main(int argc, char** argv)
{
	Product product;
	const char* product_path;
	char* product_name;
	char** band_names;
	int num_bands;

	printf("PATH: %s\n", getenv("PATH"));

	if (argc <= 1) {
		fprintf(stderr, "usage: beam_capi_test <product-file>\n");
		PRESS_ENTER();
		return 1;
	}

	product_path = argv[1];

	if (!beam_create_jvm_with_defaults()) {
		CONFIRM("error 2");
		return 2;
	}

	product = ProductIO_readProduct2(product_path);
	if (product == NULL) {
		CONFIRM("error 3");
		return 3;
	}

	product_name = Product_getName(product);
    band_names = Product_getBandNames(product, &num_bands);

	{
		int i;

		printf("product_path = %s\n", product_path);
		printf("product_name = %s\n", product_name);
		printf("num_bands = %d\n", num_bands);

		for (i = 0; i < num_bands; i++) {
			printf("band_names[%d] = %s\n", i, band_names[i]);
		}
	}

	if (!beam_destroy_jvm()) {
		CONFIRM("error 4");
		return 4;
	}

	CONFIRM("success");
	return 0;
}

