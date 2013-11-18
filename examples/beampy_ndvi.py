import sys
import numpy

from beampy import Product
from beampy import ProductData
from beampy import ProductIO
from beampy import ProductUtils
from beampy import FlagCoding

if len(sys.argv) != 2:
    print("usage: %s <file>" % sys.argv[0])
    sys.exit(1)

file = sys.argv[1]

product = ProductIO.readProduct(file)
width = product.getSceneRasterWidth()
height = product.getSceneRasterHeight()
name = product.getName()
description = product.getDescription()
band_names = product.getBandNames()

print("Product:     %s, %s" % (name, description))
print("Raster size: %d x %d pixels" % (width, height))
print("Start time:  " + str(product.getStartTime()))
print("End time:    " + str(product.getEndTime()))
print("Bands:       %s" % (band_names))
sys.exit(0)

b7 = product.getBand('radiance_7')
b10 = product.getBand('radiance_10')
ndviProduct = Product.newProduct('NDVI', 'NDVI', width, height)
ndviBand = ndviProduct.addNewBand('ndvi', ProductData.TYPE_FLOAT32)
ndviFlagsBand = ndviProduct.addNewBand('ndvi_flags', ProductData.TYPE_UINT8)
writer = ProductIO.getProductWriter('BEAM-DIMAP')

ProductUtils.copyGeoCoding(product, ndviProduct)

ndviFlagCoding = FlagCoding.newFlagCoding('ndvi_flags')
ndviFlagCoding.addFlag("NDVI_LOW", 1, "NDVI below 0")
ndviFlagCoding.addFlag("NDVI_HIGH", 2, "NDVI above 1")
ndviProduct.getFlagCodingGroup().add(ndviFlagCoding)

ndviFlagsBand.setSampleCoding(ndviFlagCoding)

ndviProduct.setProductWriter(writer)
ndviProduct.writeHeader('ndvi.dim')

r7 = numpy.zeros(width, dtype=numpy.float32)
r10 = numpy.zeros(width, dtype=numpy.float32)

for y in range(height):
    print("processing line ", y, " of ", height)
    r7 = b7.readPixelsFloat(0, y, width, 1, r7)
    r10 = b10.readPixelsFloat(0, y, width, 1, r10)
    ndvi = (r10 - r7) / (r10 + r7)
    ndviBand.writePixelsFloat(0, y, width, 1, ndvi)
    ndviLow = ndvi < 0.0
    ndviHigh = ndvi > 1.0
    ndviFlags = ndviLow + 2 * ndviHigh
    ndviFlagsBand.writePixelsInt(0, y, width, 1, ndviFlags)

ndviProduct.closeIO()
