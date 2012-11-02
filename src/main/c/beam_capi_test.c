
#include <stdio.h>
#include "gen/beam_capi.h"

int main(int argc, char** argv)
{
	Product product;
	const char* product_path;
	char* product_name;
	char** band_names;
	int num_bands;

	if (argc <= 1) {
		fprintf(stderr, "usage: beam_capi_test <product-file>");
		return 1;
	}

	product_path = argv[1];

	if (!beam_create_jvm_with_defaults()) {
		return 2;
	}

	product = ProductIO_readProduct2(argv[1]);
	if (product == NULL) {
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
		return 4;
	}

	return 0;
}

