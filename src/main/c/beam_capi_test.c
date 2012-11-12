
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
	int num_bands, width, height;

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

	product = ProductIO_readProduct(product_path);
	if (product == NULL) {
		CONFIRM("error 3");
		return 3;
	}

	product_name = Product_getName(product);
    band_names = Product_getBandNames(product, &num_bands);
    width = Product_getSceneRasterWidth(product);
    height = Product_getSceneRasterHeight(product);

	{
		int i;

		printf("product_path = %s\n", product_path);
		printf("product_name = %s\n", product_name);
		printf("num_bands = %d\n", num_bands);

		for (i = 0; i < num_bands; i++) {
			printf("band_names[%d] = %s\n", i, band_names[i]);
		}
	}

	{
	    Band r6 = Product_getBand(product, "radiance_6");
	    Band r7 = Product_getBand(product, "radiance_7");
	    Product ndviProduct = Product_newProduct("NDVI", "NDVI", width, height);
	    Band ndviBand = Product_addNewBand(ndviProduct, "ndvi", 30);
	    ProductWriter writer = ProductIO_getProductWriter("BEAM-DIMAP");

	    float* r6Buf = (float*) malloc(width * sizeof (float));
	    float* r7Buf = (float*) malloc(width * sizeof (float));
	    float* ndviBuf = (float*) malloc(width * sizeof (float));
	    int x, y;

		ProductUtils_copyGeoCoding(product, ndviProduct);

	    Product_setProductWriter(ndviProduct, writer);
	    Product_writeHeader(ndviProduct, String_newString("ndvi.dim"));

		for (y = 0; y < height; y++) {
	        Band_readPixelsFloat(r6, 0, y, width, 1, r6Buf, width, NULL);
	        Band_readPixelsFloat(r7, 0, y, width, 1, r7Buf, width, NULL);
    	    for (x = 0; x < width; x++) {
				ndviBuf[x] = 0.5F * (r6Buf[x] + r7Buf[x]);
            }
	        Band_writePixelsFloat(ndviBand, 0, y, width, 1, ndviBuf, width);
	    }

        free(r6Buf);
        free(r7Buf);
        free(ndviBuf);

	    Product_closeIO(ndviProduct);
	}

	Product_closeIO(product);


	if (!beam_destroy_jvm()) {
		CONFIRM("error 4");
		return 4;
	}

	CONFIRM("success");
	return 0;
}

