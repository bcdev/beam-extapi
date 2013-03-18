import sys
import beampy
import numpy
import itertools as it

from beampy import String
from beampy import Product
from beampy import ProductData
from beampy import ProductIO
from beampy import ProductUtils

if len(sys.argv) != 2:
    printf("usage: %s <file>", sys.argv[0]);
    sys.exit(1)

product = ProductIO.readProduct(sys.argv[1])
width = product.getSceneRasterWidth()
height = product.getSceneRasterHeight()
name = product.getName()
desc = product.getDescription()
band_names = product.getBandNames()

print("Product: %s, %d x %d pixels, %s" % (name, width, height, desc))
print("Bands:   %s" % (band_names))

b7 = product.getBand('radiance_7')
b10 = product.getBand('radiance_10')
ndviProduct = Product.newProduct('NDVI', 'NDVI', width, height)
ndviBand = ndviProduct.addNewBand('ndvi', ProductData.TYPE_FLOAT32)
writer = ProductIO.getProductWriter('BEAM-DIMAP')

ProductUtils.copyGeoCoding(product, ndviProduct)

ndviProduct.setProductWriter(writer)
ndviProduct.writeHeader(String.newString('ndvi.dim'))

r7  = numpy.zeros(width, dtype=numpy.float32)
r10 = numpy.zeros(width, dtype=numpy.float32)

for y in range(height):
    r7  = b7.readPixelsFloat(0, y, width, 1, r7)
    r10 = b10.readPixelsFloat(0, y, width, 1, r10)
    print("processing line ", y, " of ", height)
    ndvi = (r10 - r7) / (r10 + r7)
    ndviBand.writePixelsFloat(0, y, width, 1, ndvi)

ndviProduct.closeIO()
