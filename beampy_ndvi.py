import sys
import beampy
import numpy
import itertools as it

from beampy import String
from beampy import Product
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

r7 = product.getBand('radiance_7')
r10 = product.getBand('radiance_10')
ndviProduct = Product.newProduct('NDVI', 'NDVI', width, height)
ndviBand = ndviProduct.addNewBand('ndvi', ProductData.TYPE_FLOAT32)
writer = ProductIO.getProductWriter('BEAM-DIMAP')

ProductUtils.copyGeoCoding(product, ndviProduct)

ndviProduct.setProductWriter(writer)
ndviProduct.writeHeader(String.newString('ndvi.dim'))

r7Buf = None
r10Buf = None

for y in range(height):
    r7Buf = r7.readPixelsFloat(0, y, width, 1, r7Buf)
    a7 = numpy.frombuffer(r7Buf, dtype='float32')

    r10Buf = r10.readPixelsFloat(0, y, width, 1, r10Buf)
    a10 = numpy.frombuffer(r10Buf, dtype='float32')

    ndvi = (a10 - a7) / (a10 + a7)
    ndviBand.writePixelsFloat(0, y, width, 1, ndvi)


ndviProduct.closeIO()
