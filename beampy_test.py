import sys
import beampy

from beampy import String
from beampy import Product
from beampy import ProductIO
from beampy import ProductUtils


dir(beampy)

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

r6 = product.getBand('radiance_6')
r7 = product.getBand('radiance_7')
ndviProduct = Product.newProduct('NDVI', 'NDVI', width, height)
ndviBand = ndviProduct.addNewBand('ndvi', 30)
writer = ProductIO.getProductWriter('BEAM-DIMAP')

import array
from itertools import repeat
r6Buf = array.array('f', repeat(0, width))
r7Buf = array.array('f', repeat(0, width))
ndviBuf = array.array('f', repeat(0, width))

ProductUtils.copyGeoCoding(product, ndviProduct)

ndviProduct.setProductWriter(writer)
ndviProduct.writeHeader(String.newString('ndvi.dim'))

ndviProduct.closeIO()


