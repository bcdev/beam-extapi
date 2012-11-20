from _beampy import *

class Shape:
    def __init__(self, obj):
        self._obj = obj


class MapTransform:
    def __init__(self, obj):
        self._obj = obj


class ImageGeometry:
    def __init__(self, obj):
        self._obj = obj

    def newImageGeometry(bounds, mapCrs, image2map):
        return ImageGeometry(ImageGeometry_newImageGeometry(bounds._obj, mapCrs._obj, image2map._obj))

    def getImage2MapTransform(self):
        return AffineTransform(ImageGeometry_getImage2MapTransform(self._obj))

    def getImageRect(self):
        return Rectangle(ImageGeometry_getImageRect(self._obj))

    def getMapCrs(self):
        return CoordinateReferenceSystem(ImageGeometry_getMapCrs(self._obj))

    def changeYAxisDirection(self):
        ImageGeometry_changeYAxisDirection(self._obj)
        return

    def calculateEastingNorthing(sourceProduct, targetCrs, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY):
        return Point2D(ImageGeometry_calculateEastingNorthing(sourceProduct._obj, targetCrs._obj, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY))

    def calculateProductSize(sourceProduct, targetCrs, pixelSizeX, pixelSizeY):
        return Rectangle(ImageGeometry_calculateProductSize(sourceProduct._obj, targetCrs._obj, pixelSizeX, pixelSizeY))

    def createTargetGeometry(sourceProduct, targetCrs, pixelSizeX, pixelSizeY, width, height, orientation, easting, northing, referencePixelX, referencePixelY):
        return ImageGeometry(ImageGeometry_createTargetGeometry(sourceProduct._obj, targetCrs._obj, pixelSizeX._obj, pixelSizeY._obj, width._obj, height._obj, orientation._obj, easting._obj, northing._obj, referencePixelX._obj, referencePixelY._obj))

    def createCollocationTargetGeometry(targetProduct, collocationProduct):
        return ImageGeometry(ImageGeometry_createCollocationTargetGeometry(targetProduct._obj, collocationProduct._obj))


class GeoCoding:
    def __init__(self, obj):
        self._obj = obj

    def isCrossingMeridianAt180(self):
        return GeoCoding_isCrossingMeridianAt180(self._obj)

    def canGetPixelPos(self):
        return GeoCoding_canGetPixelPos(self._obj)

    def canGetGeoPos(self):
        return GeoCoding_canGetGeoPos(self._obj)

    def getPixelPos(self, geoPos, pixelPos):
        return PixelPos(GeoCoding_getPixelPos(self._obj, geoPos._obj, pixelPos._obj))

    def getGeoPos(self, pixelPos, geoPos):
        return GeoPos(GeoCoding_getGeoPos(self._obj, pixelPos._obj, geoPos._obj))

    def dispose(self):
        GeoCoding_dispose(self._obj)
        return

    def getImageCRS(self):
        return CoordinateReferenceSystem(GeoCoding_getImageCRS(self._obj))

    def getMapCRS(self):
        return CoordinateReferenceSystem(GeoCoding_getMapCRS(self._obj))

    def getGeoCRS(self):
        return CoordinateReferenceSystem(GeoCoding_getGeoCRS(self._obj))

    def getImageToMapTransform(self):
        return MathTransform(GeoCoding_getImageToMapTransform(self._obj))


class Parser:
    def __init__(self, obj):
        self._obj = obj


class ProductData:
    def __init__(self, obj):
        self._obj = obj

    def createInstance(type):
        return ProductData(ProductData_createInstance1(type))

    def createInstance(type, numElems):
        return ProductData(ProductData_createInstance2(type, numElems))

    def createInstance(type, data):
        return ProductData(ProductData_createInstance3(type, data._obj))

    def createInstance(elems):
        return ProductData(ProductData_createInstance5(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance1(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance10(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance3(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance8(elems))

    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance2(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance9(elems))

    def createInstance(strData):
        return ProductData(ProductData_createInstance4(strData))

    def createInstance(elems):
        return ProductData(ProductData_createInstance7(elems))

    def createInstance(elems):
        return ProductData(ProductData_createInstance6(elems))

    def getType(self):
        return ProductData_getType1(self._obj)

    def getElemSize(type):
        return ProductData_getElemSize2(type)

    def getElemSize(self):
        return ProductData_getElemSize1(self._obj)

    def getTypeString(type):
        return ProductData_getTypeString2(type)

    def getType(type):
        return ProductData_getType2(type)

    def getTypeString(self):
        return ProductData_getTypeString1(self._obj)

    def isInt(self):
        return ProductData_isInt(self._obj)

    def isIntType(type):
        return ProductData_isIntType(type)

    def isSigned(self):
        return ProductData_isSigned(self._obj)

    def isUnsigned(self):
        return ProductData_isUnsigned(self._obj)

    def isUIntType(type):
        return ProductData_isUIntType(type)

    def isFloatingPointType(type):
        return ProductData_isFloatingPointType(type)

    def isScalar(self):
        return ProductData_isScalar(self._obj)

    def getNumElems(self):
        return ProductData_getNumElems(self._obj)

    def getElemInt(self):
        return ProductData_getElemInt(self._obj)

    def getElemUInt(self):
        return ProductData_getElemUInt(self._obj)

    def getElemFloat(self):
        return ProductData_getElemFloat(self._obj)

    def getElemDouble(self):
        return ProductData_getElemDouble(self._obj)

    def getElemString(self):
        return ProductData_getElemString(self._obj)

    def getElemBoolean(self):
        return ProductData_getElemBoolean(self._obj)

    def getElemIntAt(self, index):
        return ProductData_getElemIntAt(self._obj, index)

    def getElemUIntAt(self, index):
        return ProductData_getElemUIntAt(self._obj, index)

    def getElemFloatAt(self, index):
        return ProductData_getElemFloatAt(self._obj, index)

    def getElemDoubleAt(self, index):
        return ProductData_getElemDoubleAt(self._obj, index)

    def getElemStringAt(self, index):
        return ProductData_getElemStringAt(self._obj, index)

    def getElemBooleanAt(self, index):
        return ProductData_getElemBooleanAt(self._obj, index)

    def setElemInt(self, value):
        ProductData_setElemInt(self._obj, value)
        return

    def setElemUInt(self, value):
        ProductData_setElemUInt(self._obj, value)
        return

    def setElemFloat(self, value):
        ProductData_setElemFloat(self._obj, value)
        return

    def setElemDouble(self, value):
        ProductData_setElemDouble(self._obj, value)
        return

    def setElemString(self, value):
        ProductData_setElemString(self._obj, value)
        return

    def setElemBoolean(self, value):
        ProductData_setElemBoolean(self._obj, value)
        return

    def setElemIntAt(self, index, value):
        ProductData_setElemIntAt(self._obj, index, value)
        return

    def setElemUIntAt(self, index, value):
        ProductData_setElemUIntAt(self._obj, index, value)
        return

    def setElemFloatAt(self, index, value):
        ProductData_setElemFloatAt(self._obj, index, value)
        return

    def setElemDoubleAt(self, index, value):
        ProductData_setElemDoubleAt(self._obj, index, value)
        return

    def setElemStringAt(self, index, value):
        ProductData_setElemStringAt(self._obj, index, value)
        return

    def setElemBooleanAt(self, index, value):
        ProductData_setElemBooleanAt(self._obj, index, value)
        return

    def getElems(self):
        return Object(ProductData_getElems(self._obj))

    def setElems(self, data):
        ProductData_setElems(self._obj, data._obj)
        return

    def readFrom(self, input):
        ProductData_readFrom4(self._obj, input._obj)
        return

    def readFrom(self, pos, input):
        ProductData_readFrom3(self._obj, pos, input._obj)
        return

    def readFrom(self, startPos, numElems, input):
        ProductData_readFrom1(self._obj, startPos, numElems, input._obj)
        return

    def readFrom(self, startPos, numElems, input, inputPos):
        ProductData_readFrom2(self._obj, startPos, numElems, input._obj, inputPos)
        return

    def writeTo(self, output):
        ProductData_writeTo4(self._obj, output._obj)
        return

    def writeTo(self, pos, output):
        ProductData_writeTo3(self._obj, pos, output._obj)
        return

    def writeTo(self, startPos, numElems, output):
        ProductData_writeTo1(self._obj, startPos, numElems, output._obj)
        return

    def writeTo(self, startPos, numElems, output, outputPos):
        ProductData_writeTo2(self._obj, startPos, numElems, output._obj, outputPos)
        return

    def toString(self):
        return ProductData_toString(self._obj)

    def hashCode(self):
        return ProductData_hashCode(self._obj)

    def equals(self, other):
        return ProductData_equals(self._obj, other._obj)

    def equalElems(self, other):
        return ProductData_equalElems(self._obj, other._obj)

    def dispose(self):
        ProductData_dispose(self._obj)
        return


class AffineTransform:
    def __init__(self, obj):
        self._obj = obj


class Mask:
    def __init__(self, obj):
        self._obj = obj


class Double:
    def __init__(self, obj):
        self._obj = obj


class IndexCoding:
    def __init__(self, obj):
        self._obj = obj

    def newIndexCoding(name):
        return IndexCoding(IndexCoding_newIndexCoding(name))

    def getIndex(self, name):
        return MetadataAttribute(IndexCoding_getIndex(self._obj, name))

    def getIndexNames(self):
        return IndexCoding_getIndexNames(self._obj)

    def addIndex(self, name, value, description):
        return MetadataAttribute(IndexCoding_addIndex(self._obj, name, value, description))

    def getIndexValue(self, name):
        return IndexCoding_getIndexValue(self._obj, name)

    def acceptVisitor(self, visitor):
        IndexCoding_acceptVisitor(self._obj, visitor._obj)
        return

    def addElement(self, element):
        IndexCoding_addElement(self._obj, element._obj)
        return

    def addAttribute(self, attribute):
        IndexCoding_addAttribute(self._obj, attribute._obj)
        return

    def addSample(self, name, value, description):
        return MetadataAttribute(IndexCoding_addSample(self._obj, name, value, description))

    def getSampleCount(self):
        return IndexCoding_getSampleCount(self._obj)

    def getSampleName(self, index):
        return IndexCoding_getSampleName(self._obj, index)

    def getSampleValue(self, index):
        return IndexCoding_getSampleValue(self._obj, index)

    def getElementGroup(self):
        return ProductNodeGroup(IndexCoding_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(IndexCoding_getParentElement(self._obj))

    def addElementAt(self, element, index):
        IndexCoding_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        return IndexCoding_removeElement(self._obj, element._obj)

    def getNumElements(self):
        return IndexCoding_getNumElements(self._obj)

    def getElementAt(self, index):
        return MetadataElement(IndexCoding_getElementAt(self._obj, index))

    def getElementNames(self):
        return IndexCoding_getElementNames(self._obj)

    def getElements(self):
        return MetadataElement(IndexCoding_getElements(self._obj))

    def getElement(self, name):
        return MetadataElement(IndexCoding_getElement(self._obj, name))

    def containsElement(self, name):
        return IndexCoding_containsElement(self._obj, name)

    def getElementIndex(self, element):
        return IndexCoding_getElementIndex(self._obj, element._obj)

    def removeAttribute(self, attribute):
        return IndexCoding_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        return IndexCoding_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(IndexCoding_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        return IndexCoding_getAttributeNames(self._obj)

    def getAttributes(self):
        return MetadataAttribute(IndexCoding_getAttributes(self._obj))

    def getAttribute(self, name):
        return MetadataAttribute(IndexCoding_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        return IndexCoding_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        return IndexCoding_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        return IndexCoding_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(IndexCoding_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        return IndexCoding_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        IndexCoding_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        IndexCoding_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        IndexCoding_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        return IndexCoding_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        IndexCoding_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        IndexCoding_setModified(self._obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(IndexCoding_createDeepClone(self._obj))

    def dispose(self):
        IndexCoding_dispose(self._obj)
        return

    def getOwner(self):
        return ProductNode(IndexCoding_getOwner(self._obj))

    def getName(self):
        return IndexCoding_getName(self._obj)

    def setName(self, name):
        IndexCoding_setName(self._obj, name)
        return

    def getDescription(self):
        return IndexCoding_getDescription(self._obj)

    def setDescription(self, description):
        IndexCoding_setDescription(self._obj, description)
        return

    def isModified(self):
        return IndexCoding_isModified(self._obj)

    def toString(self):
        return IndexCoding_toString(self._obj)

    def isValidNodeName(name):
        return IndexCoding_isValidNodeName(name)

    def getProduct(self):
        return Product(IndexCoding_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(IndexCoding_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(IndexCoding_getProductWriter(self._obj))

    def getDisplayName(self):
        return IndexCoding_getDisplayName(self._obj)

    def getProductRefString(self):
        return IndexCoding_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        IndexCoding_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        IndexCoding_removeFromFile(self._obj, productWriter._obj)
        return


class Term:
    def __init__(self, obj):
        self._obj = obj


class RasterDataNode:
    def __init__(self, obj):
        self._obj = obj


class PixelPos:
    def __init__(self, obj):
        self._obj = obj

    def newPixelPos():
        return PixelPos(PixelPos_newPixelPos1())

    def newPixelPos(x, y):
        return PixelPos(PixelPos_newPixelPos2(x, y))

    def isValid(self):
        return PixelPos_isValid(self._obj)

    def setInvalid(self):
        PixelPos_setInvalid(self._obj)
        return

    def getX(self):
        return PixelPos_getX(self._obj)

    def getY(self):
        return PixelPos_getY(self._obj)

    def setLocation(self, arg0, arg1):
        PixelPos_setLocation1(self._obj, arg0, arg1)
        return

    def setLocation(self, arg0, arg1):
        PixelPos_setLocation2(self._obj, arg0, arg1)
        return

    def toString(self):
        return PixelPos_toString(self._obj)

    def setLocation(self, arg0):
        PixelPos_setLocation3(self._obj, arg0._obj)
        return

    def distanceSq(arg0, arg1, arg2, arg3):
        return PixelPos_distanceSq2(arg0, arg1, arg2, arg3)

    def distance(arg0, arg1, arg2, arg3):
        return PixelPos_distance2(arg0, arg1, arg2, arg3)

    def distanceSq(self, arg0, arg1):
        return PixelPos_distanceSq1(self._obj, arg0, arg1)

    def distanceSq(self, arg0):
        return PixelPos_distanceSq3(self._obj, arg0._obj)

    def distance(self, arg0, arg1):
        return PixelPos_distance1(self._obj, arg0, arg1)

    def distance(self, arg0):
        return PixelPos_distance3(self._obj, arg0._obj)

    def clone(self):
        return Object(PixelPos_clone(self._obj))

    def hashCode(self):
        return PixelPos_hashCode(self._obj)

    def equals(self, arg0):
        return PixelPos_equals(self._obj, arg0._obj)


class Product_AutoGrouping:
    def __init__(self, obj):
        self._obj = obj


class ImageOutputStream:
    def __init__(self, obj):
        self._obj = obj


class Stx:
    def __init__(self, obj):
        self._obj = obj


class Rectangle:
    def __init__(self, obj):
        self._obj = obj


class ProductIO:
    def __init__(self, obj):
        self._obj = obj

    def getProductReader(formatName):
        return ProductReader(ProductIO_getProductReader(formatName))

    def getProductWriterExtensions(formatName):
        return ProductIO_getProductWriterExtensions(formatName)

    def getProductWriter(formatName):
        return ProductWriter(ProductIO_getProductWriter(formatName))

    def readProduct(filePath):
        return Product(ProductIO_readProduct(filePath))

    def getProductReaderForInput(input):
        return ProductReader(ProductIO_getProductReaderForInput(input._obj))

    def writeProduct(product, filePath, formatName):
        ProductIO_writeProduct(product._obj, filePath, formatName)
        return


class ProductNode:
    def __init__(self, obj):
        self._obj = obj


class Dimension:
    def __init__(self, obj):
        self._obj = obj


class AngularDirection:
    def __init__(self, obj):
        self._obj = obj

    def newAngularDirection(azimuth, zenith):
        return AngularDirection(AngularDirection_newAngularDirection(azimuth, zenith))

    def equals(self, obj):
        return AngularDirection_equals(self._obj, obj._obj)

    def toString(self):
        return AngularDirection_toString(self._obj)


class SimpleFeatureType:
    def __init__(self, obj):
        self._obj = obj


class SampleCoding:
    def __init__(self, obj):
        self._obj = obj


class Object:
    def __init__(self, obj):
        self._obj = obj


class ProductReader:
    def __init__(self, obj):
        self._obj = obj

    def getReaderPlugIn(self):
        return ProductReaderPlugIn(ProductReader_getReaderPlugIn(self._obj))

    def getInput(self):
        return Object(ProductReader_getInput(self._obj))

    def getSubsetDef(self):
        return ProductSubsetDef(ProductReader_getSubsetDef(self._obj))

    def readProductNodes(self, input, subsetDef):
        return Product(ProductReader_readProductNodes(self._obj, input._obj, subsetDef._obj))

    def readBandRasterData(self, destBand, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer, pm):
        ProductReader_readBandRasterData(self._obj, destBand._obj, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer._obj, pm._obj)
        return

    def close(self):
        ProductReader_close(self._obj)
        return


class ProductReaderPlugIn:
    def __init__(self, obj):
        self._obj = obj


class Integer:
    def __init__(self, obj):
        self._obj = obj


class ProductData_UTC:
    def __init__(self, obj):
        self._obj = obj


class Band:
    def __init__(self, obj):
        self._obj = obj

    def newBand(name, dataType, width, height):
        return Band(Band_newBand(name, dataType, width, height))

    def getFlagCoding(self):
        return FlagCoding(Band_getFlagCoding(self._obj))

    def isFlagBand(self):
        return Band_isFlagBand(self._obj)

    def getIndexCoding(self):
        return IndexCoding(Band_getIndexCoding(self._obj))

    def isIndexBand(self):
        return Band_isIndexBand(self._obj)

    def getSampleCoding(self):
        return SampleCoding(Band_getSampleCoding(self._obj))

    def setSampleCoding(self, sampleCoding):
        Band_setSampleCoding(self._obj, sampleCoding._obj)
        return

    def getSpectralBandIndex(self):
        return Band_getSpectralBandIndex(self._obj)

    def setSpectralBandIndex(self, spectralBandIndex):
        Band_setSpectralBandIndex(self._obj, spectralBandIndex)
        return

    def getSpectralWavelength(self):
        return Band_getSpectralWavelength(self._obj)

    def setSpectralWavelength(self, spectralWavelength):
        Band_setSpectralWavelength(self._obj, spectralWavelength)
        return

    def getSpectralBandwidth(self):
        return Band_getSpectralBandwidth(self._obj)

    def setSpectralBandwidth(self, spectralBandwidth):
        Band_setSpectralBandwidth(self._obj, spectralBandwidth)
        return

    def getSolarFlux(self):
        return Band_getSolarFlux(self._obj)

    def setSolarFlux(self, solarFlux):
        Band_setSolarFlux(self._obj, solarFlux)
        return

    def acceptVisitor(self, visitor):
        Band_acceptVisitor(self._obj, visitor._obj)
        return

    def toString(self):
        return Band_toString(self._obj)

    def removeFromFile(self, productWriter):
        Band_removeFromFile(self._obj, productWriter._obj)
        return

    def dispose(self):
        Band_dispose(self._obj)
        return

    def getSceneRasterData(self):
        return ProductData(Band_getSceneRasterData(self._obj))

    def getPixelInt(self, x, y):
        return Band_getPixelInt(self._obj, x, y)

    def getPixelFloat(self, x, y):
        return Band_getPixelFloat(self._obj, x, y)

    def getPixelDouble(self, x, y):
        return Band_getPixelDouble(self._obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        Band_setPixelInt(self._obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        Band_setPixelFloat(self._obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        Band_setPixelDouble(self._obj, x, y, pixelValue)
        return

    def setPixels(self, x, y, w, h, pixels):
        Band_setPixelsInt(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        Band_setPixelsFloat(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        Band_setPixelsDouble(self._obj, x, y, w, h, pixels)
        return

    def ensureRasterData(self):
        Band_ensureRasterData(self._obj)
        return

    def unloadRasterData(self):
        Band_unloadRasterData(self._obj)
        return

    def getViewModeId(self, bandName):
        return Band_getViewModeId(self._obj, bandName)

    def getSceneRasterWidth(self):
        return Band_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        return Band_getSceneRasterHeight(self._obj)

    def getRasterWidth(self):
        return Band_getRasterWidth(self._obj)

    def getRasterHeight(self):
        return Band_getRasterHeight(self._obj)

    def setModified(self, modified):
        Band_setModified(self._obj, modified)
        return

    def getGeoCoding(self):
        return GeoCoding(Band_getGeoCoding(self._obj))

    def setGeoCoding(self, geoCoding):
        Band_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getPointing(self):
        return Pointing(Band_getPointing(self._obj))

    def canBeOrthorectified(self):
        return Band_canBeOrthorectified(self._obj)

    def isFloatingPointType(self):
        return Band_isFloatingPointType(self._obj)

    def getGeophysicalDataType(self):
        return Band_getGeophysicalDataType(self._obj)

    def getScalingFactor(self):
        return Band_getScalingFactor(self._obj)

    def setScalingFactor(self, scalingFactor):
        Band_setScalingFactor(self._obj, scalingFactor)
        return

    def getScalingOffset(self):
        return Band_getScalingOffset(self._obj)

    def setScalingOffset(self, scalingOffset):
        Band_setScalingOffset(self._obj, scalingOffset)
        return

    def isLog10Scaled(self):
        return Band_isLog10Scaled(self._obj)

    def setLog10Scaled(self, log10Scaled):
        Band_setLog10Scaled(self._obj, log10Scaled)
        return

    def isScalingApplied(self):
        return Band_isScalingApplied(self._obj)

    def isValidMaskProperty(propertyName):
        return Band_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        return Band_isNoDataValueSet(self._obj)

    def clearNoDataValue(self):
        Band_clearNoDataValue(self._obj)
        return

    def isNoDataValueUsed(self):
        return Band_isNoDataValueUsed(self._obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        Band_setNoDataValueUsed(self._obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        return Band_getNoDataValue(self._obj)

    def setNoDataValue(self, noDataValue):
        Band_setNoDataValue(self._obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        return Band_getGeophysicalNoDataValue(self._obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        Band_setGeophysicalNoDataValue(self._obj, noDataValue)
        return

    def getValidPixelExpression(self):
        return Band_getValidPixelExpression(self._obj)

    def setValidPixelExpression(self, validPixelExpression):
        Band_setValidPixelExpression(self._obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        return Band_isValidMaskUsed(self._obj)

    def resetValidMask(self):
        Band_resetValidMask(self._obj)
        return

    def getValidMaskExpression(self):
        return Band_getValidMaskExpression(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        Band_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        return Band_hasRasterData(self._obj)

    def getRasterData(self):
        return ProductData(Band_getRasterData(self._obj))

    def setRasterData(self, rasterData):
        Band_setRasterData(self._obj, rasterData._obj)
        return

    def loadRasterData(self):
        Band_loadRasterData(self._obj)
        return

    def isPixelValid(self, x, y):
        return Band_isPixelValid(self._obj, x, y)

    def getSampleInt(self, x, y):
        return Band_getSampleInt(self._obj, x, y)

    def getSampleFloat(self, x, y):
        return Band_getSampleFloat(self._obj, x, y)

    def getPixels(self, x, y, w, h, pixels):
        return Band_getPixelsInt(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return Band_getPixelsFloat(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return Band_getPixelsDouble(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return Band_readPixelsInt(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return Band_readPixelsFloat(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return Band_readPixelsDouble(self._obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        Band_writePixelsInt(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        Band_writePixelsFloat(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        Band_writePixelsDouble(self._obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return Band_readValidMask(self._obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        Band_readRasterDataFully(self._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        Band_readRasterData(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def writeRasterDataFully(self):
        Band_writeRasterDataFully(self._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        Band_writeRasterData(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def createCompatibleRasterData(self):
        return ProductData(Band_createCompatibleRasterData(self._obj))

    def createCompatibleSceneRasterData(self):
        return ProductData(Band_createCompatibleSceneRasterData(self._obj))

    def createCompatibleRasterData(self, width, height):
        return ProductData(Band_createCompatibleRasterDataForRect(self._obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        return Band_isCompatibleRasterData(self._obj, rasterData._obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        Band_checkCompatibleRasterData(self._obj, rasterData._obj, w, h)
        return

    def hasIntPixels(self):
        return Band_hasIntPixels(self._obj)

    def createTransectProfileData(self, shape):
        return TransectProfileData(Band_createTransectProfileData(self._obj, shape._obj))

    def getImageInfo(self):
        return ImageInfo(Band_getImageInfo(self._obj))

    def setImageInfo(self, imageInfo):
        Band_setImageInfo(self._obj, imageInfo._obj)
        return

    def fireImageInfoChanged(self):
        Band_fireImageInfoChanged(self._obj)
        return

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        return ImageInfo(Band_createDefaultImageInfo(self._obj, histoSkipAreas, histogram._obj))

    def getOverlayMaskGroup(self):
        return ProductNodeGroup(Band_getOverlayMaskGroup(self._obj))

    def createColorIndexedImage(self, pm):
        return BufferedImage(Band_createColorIndexedImage(self._obj, pm._obj))

    def createRgbImage(self, pm):
        return BufferedImage(Band_createRgbImage(self._obj, pm._obj))

    def createPixelValidator(self, lineOffset, roi):
        return IndexValidator(Band_createPixelValidator(self._obj, lineOffset, roi._obj))

    def scale(self, v):
        return Band_scale(self._obj, v)

    def scaleInverse(self, v):
        return Band_scaleInverse(self._obj, v)

    def getPixelString(self, x, y):
        return Band_getPixelString(self._obj, x, y)

    def isSourceImageSet(self):
        return Band_isSourceImageSet(self._obj)

    def getSourceImage(self):
        return MultiLevelImage(Band_getSourceImage(self._obj))

    def isGeophysicalImageSet(self):
        return Band_isGeophysicalImageSet(self._obj)

    def getGeophysicalImage(self):
        return MultiLevelImage(Band_getGeophysicalImage(self._obj))

    def isValidMaskImageSet(self):
        return Band_isValidMaskImageSet(self._obj)

    def getValidMaskImage(self):
        return MultiLevelImage(Band_getValidMaskImage(self._obj))

    def isStxSet(self):
        return Band_isStxSet(self._obj)

    def getStx(self):
        return Stx(Band_getStx(self._obj))

    def setStx(self, stx):
        Band_setStx(self._obj, stx._obj)
        return

    def getValidShape(self):
        return Shape(Band_getValidShape(self._obj))

    def getDataType(self):
        return Band_getDataType(self._obj)

    def getNumDataElems(self):
        return Band_getNumDataElems(self._obj)

    def setData(self, data):
        Band_setData(self._obj, data._obj)
        return

    def getData(self):
        return ProductData(Band_getData(self._obj))

    def setDataElems(self, elems):
        Band_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        return Object(Band_getDataElems(self._obj))

    def getDataElemSize(self):
        return Band_getDataElemSize(self._obj)

    def setReadOnly(self, readOnly):
        Band_setReadOnly(self._obj, readOnly)
        return

    def isReadOnly(self):
        return Band_isReadOnly(self._obj)

    def setUnit(self, unit):
        Band_setUnit(self._obj, unit)
        return

    def getUnit(self):
        return Band_getUnit(self._obj)

    def fireProductNodeDataChanged(self):
        Band_fireProductNodeDataChanged(self._obj)
        return

    def createCompatibleProductData(self, numElems):
        return ProductData(Band_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        return ProductNode(Band_getOwner(self._obj))

    def getName(self):
        return Band_getName(self._obj)

    def setName(self, name):
        Band_setName(self._obj, name)
        return

    def getDescription(self):
        return Band_getDescription(self._obj)

    def setDescription(self, description):
        Band_setDescription(self._obj, description)
        return

    def isModified(self):
        return Band_isModified(self._obj)

    def isValidNodeName(name):
        return Band_isValidNodeName(name)

    def getProduct(self):
        return Product(Band_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(Band_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(Band_getProductWriter(self._obj))

    def getDisplayName(self):
        return Band_getDisplayName(self._obj)

    def getProductRefString(self):
        return Band_getProductRefString(self._obj)


class ColorPaletteDef_Point:
    def __init__(self, obj):
        self._obj = obj


class RenderedImage:
    def __init__(self, obj):
        self._obj = obj


class Placemark:
    def __init__(self, obj):
        self._obj = obj

    def newPlacemark(descriptor, feature):
        return Placemark(Placemark_newPlacemark(descriptor._obj, feature._obj))

    def createPointPlacemark(descriptor, name, label, text, pixelPos, geoPos, geoCoding):
        return Placemark(Placemark_createPointPlacemark(descriptor._obj, name, label, text, pixelPos._obj, geoPos._obj, geoCoding._obj))

    def getDescriptor(self):
        return PlacemarkDescriptor(Placemark_getDescriptor(self._obj))

    def getFeature(self):
        return SimpleFeature(Placemark_getFeature(self._obj))

    def getAttributeValue(self, attributeName):
        return Object(Placemark_getAttributeValue(self._obj, attributeName))

    def setAttributeValue(self, attributeName, attributeValue):
        Placemark_setAttributeValue(self._obj, attributeName, attributeValue._obj)
        return

    def setLabel(self, label):
        Placemark_setLabel(self._obj, label)
        return

    def getLabel(self):
        return Placemark_getLabel(self._obj)

    def setText(self, text):
        Placemark_setText(self._obj, text)
        return

    def getText(self):
        return Placemark_getText(self._obj)

    def setStyleCss(self, styleCss):
        Placemark_setStyleCss(self._obj, styleCss)
        return

    def getStyleCss(self):
        return Placemark_getStyleCss(self._obj)

    def acceptVisitor(self, visitor):
        Placemark_acceptVisitor(self._obj, visitor._obj)
        return

    def getPixelPos(self):
        return PixelPos(Placemark_getPixelPos(self._obj))

    def setPixelPos(self, pixelPos):
        Placemark_setPixelPos(self._obj, pixelPos._obj)
        return

    def getGeoPos(self):
        return GeoPos(Placemark_getGeoPos(self._obj))

    def setGeoPos(self, geoPos):
        Placemark_setGeoPos(self._obj, geoPos._obj)
        return

    def updatePositions(self):
        Placemark_updatePositions(self._obj)
        return

    def createPinFeatureType():
        return SimpleFeatureType(Placemark_createPinFeatureType())

    def createGcpFeatureType():
        return SimpleFeatureType(Placemark_createGcpFeatureType())

    def createGeometryFeatureType():
        return SimpleFeatureType(Placemark_createGeometryFeatureType())

    def createPointFeatureType(name):
        return SimpleFeatureType(Placemark_createPointFeatureType(name))

    def getOwner(self):
        return ProductNode(Placemark_getOwner(self._obj))

    def getName(self):
        return Placemark_getName(self._obj)

    def setName(self, name):
        Placemark_setName(self._obj, name)
        return

    def getDescription(self):
        return Placemark_getDescription(self._obj)

    def setDescription(self, description):
        Placemark_setDescription(self._obj, description)
        return

    def isModified(self):
        return Placemark_isModified(self._obj)

    def setModified(self, modified):
        Placemark_setModified(self._obj, modified)
        return

    def toString(self):
        return Placemark_toString(self._obj)

    def dispose(self):
        Placemark_dispose(self._obj)
        return

    def isValidNodeName(name):
        return Placemark_isValidNodeName(name)

    def getProduct(self):
        return Product(Placemark_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(Placemark_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(Placemark_getProductWriter(self._obj))

    def getDisplayName(self):
        return Placemark_getDisplayName(self._obj)

    def getProductRefString(self):
        return Placemark_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        Placemark_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        Placemark_removeFromFile(self._obj, productWriter._obj)
        return


class IndexValidator:
    def __init__(self, obj):
        self._obj = obj


class Area:
    def __init__(self, obj):
        self._obj = obj


class ComponentColorModel:
    def __init__(self, obj):
        self._obj = obj


class Iterator:
    def __init__(self, obj):
        self._obj = obj


class MathTransform:
    def __init__(self, obj):
        self._obj = obj


class CoordinateReferenceSystem:
    def __init__(self, obj):
        self._obj = obj


class ProductWriterPlugIn:
    def __init__(self, obj):
        self._obj = obj


class File:
    def __init__(self, obj):
        self._obj = obj


class GeoPos:
    def __init__(self, obj):
        self._obj = obj

    def newGeoPos(lat, lon):
        return GeoPos(GeoPos_newGeoPos(lat, lon))

    def getLat(self):
        return GeoPos_getLat(self._obj)

    def getLon(self):
        return GeoPos_getLon(self._obj)

    def setLocation(self, lat, lon):
        GeoPos_setLocation(self._obj, lat, lon)
        return

    def isValid(self):
        return GeoPos_isValid(self._obj)

    def areValid(a):
        return GeoPos_areValid(a._obj)

    def setInvalid(self):
        GeoPos_setInvalid(self._obj)
        return

    def equals(self, obj):
        return GeoPos_equals(self._obj, obj._obj)

    def hashCode(self):
        return GeoPos_hashCode(self._obj)

    def toString(self):
        return GeoPos_toString(self._obj)

    def normalize(self):
        GeoPos_normalize(self._obj)
        return

    def normalizeLon(lon):
        return GeoPos_normalizeLon(lon)

    def getLatString(self):
        return GeoPos_getLatString(self._obj)

    def getLonString(self):
        return GeoPos_getLonString(self._obj)


class ProductNodeGroup:
    def __init__(self, obj):
        self._obj = obj

    def newProductNodeGroup(name):
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup1(name))

    def newProductNodeGroup(owner, name, takingOverNodeOwnership):
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup2(owner._obj, name, takingOverNodeOwnership))

    def isTakingOverNodeOwnership(self):
        return ProductNodeGroup_isTakingOverNodeOwnership(self._obj)

    def getNodeCount(self):
        return ProductNodeGroup_getNodeCount(self._obj)

    def get(self, index):
        return T(ProductNodeGroup_get1(self._obj, index))

    def getNodeDisplayNames(self):
        return ProductNodeGroup_getNodeDisplayNames(self._obj)

    def getNodeNames(self):
        return ProductNodeGroup_getNodeNames(self._obj)

    def toArray(self):
        return ProductNode(ProductNodeGroup_toArray1(self._obj))

    def toArray(self, array):
        return T(ProductNodeGroup_toArray2(self._obj, array._obj))

    def indexOf(self, name):
        return ProductNodeGroup_indexOf2(self._obj, name)

    def indexOf(self, element):
        return ProductNodeGroup_indexOf1(self._obj, element._obj)

    def getByDisplayName(self, displayName):
        return T(ProductNodeGroup_getByDisplayName(self._obj, displayName))

    def get(self, name):
        return T(ProductNodeGroup_get2(self._obj, name))

    def contains(self, name):
        return ProductNodeGroup_contains2(self._obj, name)

    def contains(self, node):
        return ProductNodeGroup_contains1(self._obj, node._obj)

    def add(self, node):
        return ProductNodeGroup_add2(self._obj, node._obj)

    def add(self, index, node):
        ProductNodeGroup_add1(self._obj, index, node._obj)
        return

    def remove(self, node):
        return ProductNodeGroup_remove(self._obj, node._obj)

    def removeAll(self):
        ProductNodeGroup_removeAll(self._obj)
        return

    def clearRemovedList(self):
        ProductNodeGroup_clearRemovedList(self._obj)
        return

    def getRemovedNodes(self):
        return Collection(ProductNodeGroup_getRemovedNodes(self._obj))

    def getRawStorageSize(self, subsetDef):
        return ProductNodeGroup_getRawStorageSize2(self._obj, subsetDef._obj)

    def setModified(self, modified):
        ProductNodeGroup_setModified(self._obj, modified)
        return

    def acceptVisitor(self, visitor):
        ProductNodeGroup_acceptVisitor(self._obj, visitor._obj)
        return

    def dispose(self):
        ProductNodeGroup_dispose(self._obj)
        return

    def updateExpression(self, oldExternalName, newExternalName):
        ProductNodeGroup_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def getOwner(self):
        return ProductNode(ProductNodeGroup_getOwner(self._obj))

    def getName(self):
        return ProductNodeGroup_getName(self._obj)

    def setName(self, name):
        ProductNodeGroup_setName(self._obj, name)
        return

    def getDescription(self):
        return ProductNodeGroup_getDescription(self._obj)

    def setDescription(self, description):
        ProductNodeGroup_setDescription(self._obj, description)
        return

    def isModified(self):
        return ProductNodeGroup_isModified(self._obj)

    def toString(self):
        return ProductNodeGroup_toString(self._obj)

    def isValidNodeName(name):
        return ProductNodeGroup_isValidNodeName(name)

    def getProduct(self):
        return Product(ProductNodeGroup_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(ProductNodeGroup_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(ProductNodeGroup_getProductWriter(self._obj))

    def getDisplayName(self):
        return ProductNodeGroup_getDisplayName(self._obj)

    def getProductRefString(self):
        return ProductNodeGroup_getProductRefString(self._obj)

    def getRawStorageSize(self):
        return ProductNodeGroup_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        ProductNodeGroup_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        ProductNodeGroup_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        ProductNodeGroup_removeFromFile(self._obj, productWriter._obj)
        return


class MapProjection:
    def __init__(self, obj):
        self._obj = obj


class ProductManager:
    def __init__(self, obj):
        self._obj = obj

    def newProductManager():
        return ProductManager(ProductManager_newProductManager())

    def getProductCount(self):
        return ProductManager_getProductCount(self._obj)

    def getProduct(self, index):
        return Product(ProductManager_getProduct(self._obj, index))

    def getProductDisplayNames(self):
        return ProductManager_getProductDisplayNames(self._obj)

    def getProductNames(self):
        return ProductManager_getProductNames(self._obj)

    def getProducts(self):
        return Product(ProductManager_getProducts(self._obj))

    def getProductByDisplayName(self, displayName):
        return Product(ProductManager_getProductByDisplayName(self._obj, displayName))

    def getProductByRefNo(self, refNo):
        return Product(ProductManager_getProductByRefNo(self._obj, refNo))

    def getProduct(self, name):
        return Product(ProductManager_getProductByName(self._obj, name))

    def getProductIndex(self, product):
        return ProductManager_getProductIndex(self._obj, product._obj)

    def containsProduct(self, name):
        return ProductManager_containsProduct(self._obj, name)

    def contains(self, product):
        return ProductManager_contains(self._obj, product._obj)

    def addProduct(self, product):
        ProductManager_addProduct(self._obj, product._obj)
        return

    def removeProduct(self, product):
        return ProductManager_removeProduct(self._obj, product._obj)

    def removeAllProducts(self):
        ProductManager_removeAllProducts(self._obj)
        return

    def addListener(self, listener):
        return ProductManager_addListener(self._obj, listener._obj)

    def removeListener(self, listener):
        return ProductManager_removeListener(self._obj, listener._obj)


class FlagCoding:
    def __init__(self, obj):
        self._obj = obj

    def newFlagCoding(name):
        return FlagCoding(FlagCoding_newFlagCoding(name))

    def getFlag(self, name):
        return MetadataAttribute(FlagCoding_getFlag(self._obj, name))

    def getFlagNames(self):
        return FlagCoding_getFlagNames(self._obj)

    def addFlag(self, name, flagMask, description):
        return MetadataAttribute(FlagCoding_addFlag(self._obj, name, flagMask, description))

    def getFlagMask(self, name):
        return FlagCoding_getFlagMask(self._obj, name)

    def acceptVisitor(self, visitor):
        FlagCoding_acceptVisitor(self._obj, visitor._obj)
        return

    def addElement(self, element):
        FlagCoding_addElement(self._obj, element._obj)
        return

    def addAttribute(self, attribute):
        FlagCoding_addAttribute(self._obj, attribute._obj)
        return

    def addSample(self, name, value, description):
        return MetadataAttribute(FlagCoding_addSample(self._obj, name, value, description))

    def getSampleCount(self):
        return FlagCoding_getSampleCount(self._obj)

    def getSampleName(self, index):
        return FlagCoding_getSampleName(self._obj, index)

    def getSampleValue(self, index):
        return FlagCoding_getSampleValue(self._obj, index)

    def getElementGroup(self):
        return ProductNodeGroup(FlagCoding_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(FlagCoding_getParentElement(self._obj))

    def addElementAt(self, element, index):
        FlagCoding_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        return FlagCoding_removeElement(self._obj, element._obj)

    def getNumElements(self):
        return FlagCoding_getNumElements(self._obj)

    def getElementAt(self, index):
        return MetadataElement(FlagCoding_getElementAt(self._obj, index))

    def getElementNames(self):
        return FlagCoding_getElementNames(self._obj)

    def getElements(self):
        return MetadataElement(FlagCoding_getElements(self._obj))

    def getElement(self, name):
        return MetadataElement(FlagCoding_getElement(self._obj, name))

    def containsElement(self, name):
        return FlagCoding_containsElement(self._obj, name)

    def getElementIndex(self, element):
        return FlagCoding_getElementIndex(self._obj, element._obj)

    def removeAttribute(self, attribute):
        return FlagCoding_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        return FlagCoding_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(FlagCoding_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        return FlagCoding_getAttributeNames(self._obj)

    def getAttributes(self):
        return MetadataAttribute(FlagCoding_getAttributes(self._obj))

    def getAttribute(self, name):
        return MetadataAttribute(FlagCoding_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        return FlagCoding_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        return FlagCoding_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        return FlagCoding_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(FlagCoding_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        return FlagCoding_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        FlagCoding_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        FlagCoding_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        FlagCoding_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        return FlagCoding_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        FlagCoding_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        FlagCoding_setModified(self._obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(FlagCoding_createDeepClone(self._obj))

    def dispose(self):
        FlagCoding_dispose(self._obj)
        return

    def getOwner(self):
        return ProductNode(FlagCoding_getOwner(self._obj))

    def getName(self):
        return FlagCoding_getName(self._obj)

    def setName(self, name):
        FlagCoding_setName(self._obj, name)
        return

    def getDescription(self):
        return FlagCoding_getDescription(self._obj)

    def setDescription(self, description):
        FlagCoding_setDescription(self._obj, description)
        return

    def isModified(self):
        return FlagCoding_isModified(self._obj)

    def toString(self):
        return FlagCoding_toString(self._obj)

    def isValidNodeName(name):
        return FlagCoding_isValidNodeName(name)

    def getProduct(self):
        return Product(FlagCoding_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(FlagCoding_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(FlagCoding_getProductWriter(self._obj))

    def getDisplayName(self):
        return FlagCoding_getDisplayName(self._obj)

    def getProductRefString(self):
        return FlagCoding_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        FlagCoding_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        FlagCoding_removeFromFile(self._obj, productWriter._obj)
        return


class IndexColorModel:
    def __init__(self, obj):
        self._obj = obj


class ImageInfo_HistogramMatching:
    def __init__(self, obj):
        self._obj = obj


class ProductNodeListener:
    def __init__(self, obj):
        self._obj = obj


class Map:
    def __init__(self, obj):
        self._obj = obj


class ProductUtils:
    def __init__(self, obj):
        self._obj = obj

    def newProductUtils():
        return ProductUtils(ProductUtils_newProductUtils())

    def createImageInfo(rasters, assignMissingImageInfos, pm):
        return ImageInfo(ProductUtils_createImageInfo(rasters._obj, assignMissingImageInfos, pm._obj))

    def createRgbImage(rasters, imageInfo, pm):
        return BufferedImage(ProductUtils_createRgbImage(rasters._obj, imageInfo._obj, pm._obj))

    def createColorIndexedImage(rasterDataNode, pm):
        return BufferedImage(ProductUtils_createColorIndexedImage(rasterDataNode._obj, pm._obj))

    def createSuitableMapInfo(product, rect, mapProjection):
        return MapInfo(ProductUtils_createSuitableMapInfo1(product._obj, rect._obj, mapProjection._obj))

    def createSuitableMapInfo(product, mapProjection, orientation, noDataValue):
        return MapInfo(ProductUtils_createSuitableMapInfo2(product._obj, mapProjection._obj, orientation, noDataValue))

    def getOutputRasterSize(product, rect, mapTransform, pixelSizeX, pixelSizeY):
        return Dimension(ProductUtils_getOutputRasterSize(product._obj, rect._obj, mapTransform._obj, pixelSizeX, pixelSizeY))

    def createMapEnvelope(product, rect, mapTransform):
        return Point2D(ProductUtils_createMapEnvelope2(product._obj, rect._obj, mapTransform._obj))

    def createMapEnvelope(product, rect, step, mapTransform):
        return Point2D(ProductUtils_createMapEnvelope1(product._obj, rect._obj, step, mapTransform._obj))

    def getMinMax(boundary):
        return Point2D(ProductUtils_getMinMax(boundary._obj))

    def createMapBoundary(product, rect, step, mapTransform):
        return Point2D(ProductUtils_createMapBoundary(product._obj, rect._obj, step, mapTransform._obj))

    def createGeoBoundary(product, step):
        return GeoPos(ProductUtils_createGeoBoundary1(product._obj, step))

    def createGeoBoundary(product, region, step):
        return GeoPos(ProductUtils_createGeoBoundary2(product._obj, region._obj, step))

    def createGeoBoundary(product, region, step, usePixelCenter):
        return GeoPos(ProductUtils_createGeoBoundary3(product._obj, region._obj, step, usePixelCenter))

    def createGeoBoundary(raster, region, step):
        return GeoPos(ProductUtils_createGeoBoundary4(raster._obj, region._obj, step))

    def createGeoBoundaryPaths(product):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths1(product._obj))

    def createGeoBoundaryPaths(product, region, step):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths2(product._obj, region._obj, step))

    def createGeoBoundaryPaths(product, region, step, usePixelCenter):
        return GeneralPath(ProductUtils_createGeoBoundaryPaths3(product._obj, region._obj, step, usePixelCenter))

    def createPixelBoundary(product, rect, step):
        return PixelPos(ProductUtils_createPixelBoundary1(product._obj, rect._obj, step))

    def createPixelBoundary(product, rect, step, usePixelCenter):
        return PixelPos(ProductUtils_createPixelBoundary2(product._obj, rect._obj, step, usePixelCenter))

    def createPixelBoundary(raster, rect, step):
        return PixelPos(ProductUtils_createPixelBoundary3(raster._obj, rect._obj, step))

    def createRectBoundary(rect, step):
        return PixelPos(ProductUtils_createRectBoundary1(rect._obj, step))

    def createRectBoundary(rect, step, usePixelCenter):
        return PixelPos(ProductUtils_createRectBoundary2(rect._obj, step, usePixelCenter))

    def copyFlagCodings(source, target):
        ProductUtils_copyFlagCodings(source._obj, target._obj)
        return

    def copyFlagCoding(sourceFlagCoding, target):
        return FlagCoding(ProductUtils_copyFlagCoding(sourceFlagCoding._obj, target._obj))

    def copyIndexCoding(sourceIndexCoding, target):
        return IndexCoding(ProductUtils_copyIndexCoding(sourceIndexCoding._obj, target._obj))

    def copyMasks(sourceProduct, targetProduct):
        ProductUtils_copyMasks(sourceProduct._obj, targetProduct._obj)
        return

    def copyOverlayMasks(sourceProduct, targetProduct):
        ProductUtils_copyOverlayMasks(sourceProduct._obj, targetProduct._obj)
        return

    def copyFlagBands(sourceProduct, targetProduct, copySourceImage):
        ProductUtils_copyFlagBands(sourceProduct._obj, targetProduct._obj, copySourceImage)
        return

    def copyTiePointGrid(gridName, sourceProduct, targetProduct):
        return TiePointGrid(ProductUtils_copyTiePointGrid(gridName, sourceProduct._obj, targetProduct._obj))

    def copyBand(sourceBandName, sourceProduct, targetProduct, copySourceImage):
        return Band(ProductUtils_copyBand2(sourceBandName, sourceProduct._obj, targetProduct._obj, copySourceImage))

    def copyBand(sourceBandName, sourceProduct, targetBandName, targetProduct, copySourceImage):
        return Band(ProductUtils_copyBand1(sourceBandName, sourceProduct._obj, targetBandName, targetProduct._obj, copySourceImage))

    def copyRasterDataNodeProperties(sourceRaster, targetRaster):
        ProductUtils_copyRasterDataNodeProperties(sourceRaster._obj, targetRaster._obj)
        return

    def copySpectralBandProperties(sourceBand, targetBand):
        ProductUtils_copySpectralBandProperties(sourceBand._obj, targetBand._obj)
        return

    def copyGeoCoding(sourceProduct, targetProduct):
        ProductUtils_copyGeoCoding(sourceProduct._obj, targetProduct._obj)
        return

    def copyTiePointGrids(sourceProduct, targetProduct):
        ProductUtils_copyTiePointGrids(sourceProduct._obj, targetProduct._obj)
        return

    def copyVectorData(sourceProduct, targetProduct):
        ProductUtils_copyVectorData(sourceProduct._obj, targetProduct._obj)
        return

    def canGetPixelPos(product):
        return ProductUtils_canGetPixelPos1(product._obj)

    def canGetPixelPos(raster):
        return ProductUtils_canGetPixelPos2(raster._obj)

    def createDensityPlotImage(raster1, sampleMin1, sampleMax1, raster2, sampleMin2, sampleMax2, roiMask, width, height, background, image, pm):
        return BufferedImage(ProductUtils_createDensityPlotImage(raster1._obj, sampleMin1, sampleMax1, raster2._obj, sampleMin2, sampleMax2, roiMask._obj, width, height, background._obj, image._obj, pm._obj))

    def overlayMasks(raster, overlayBIm, pm):
        return BufferedImage(ProductUtils_overlayMasks(raster._obj, overlayBIm._obj, pm._obj))

    def getCenterGeoPos(product):
        return GeoPos(ProductUtils_getCenterGeoPos(product._obj))

    def normalizeGeoPolygon(polygon):
        return ProductUtils_normalizeGeoPolygon(polygon._obj)

    def normalizeGeoPolygon_old(polygon):
        return ProductUtils_normalizeGeoPolygon_old(polygon._obj)

    def denormalizeGeoPolygon(polygon):
        ProductUtils_denormalizeGeoPolygon(polygon._obj)
        return

    def denormalizeGeoPos(geoPos):
        ProductUtils_denormalizeGeoPos(geoPos._obj)
        return

    def denormalizeGeoPos_old(geoPos):
        ProductUtils_denormalizeGeoPos_old(geoPos._obj)
        return

    def getRotationDirection(polygon):
        return ProductUtils_getRotationDirection(polygon._obj)

    def getAngleSum(polygon):
        return ProductUtils_getAngleSum(polygon._obj)

    def convertToPixelPath(geoPath, geoCoding):
        return GeneralPath(ProductUtils_convertToPixelPath(geoPath._obj, geoCoding._obj))

    def convertToGeoPath(shape, geoCoding):
        return GeneralPath(ProductUtils_convertToGeoPath(shape._obj, geoCoding._obj))

    def copyMetadata(source, target):
        ProductUtils_copyMetadata2(source._obj, target._obj)
        return

    def copyMetadata(source, target):
        ProductUtils_copyMetadata1(source._obj, target._obj)
        return

    def copyPreferredTileSize(sourceProduct, targetProduct):
        ProductUtils_copyPreferredTileSize(sourceProduct._obj, targetProduct._obj)
        return

    def createGeoTIFFMetadata(product):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata2(product._obj))

    def createGeoTIFFMetadata(geoCoding, width, height):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata1(geoCoding._obj, width, height))

    def areaToPath(negativeArea, deltaX):
        return GeneralPath(ProductUtils_areaToPath(negativeArea._obj, deltaX))

    def addElementToHistory(product, elem):
        ProductUtils_addElementToHistory(product._obj, elem._obj)
        return

    def removeInvalidExpressions(product):
        return ProductUtils_removeInvalidExpressions(product._obj)

    def findSuitableQuicklookBandName(product):
        return ProductUtils_findSuitableQuicklookBandName(product._obj)

    def computeSourcePixelCoordinates(sourceGeoCoding, sourceWidth, sourceHeight, destGeoCoding, destArea):
        return PixelPos(ProductUtils_computeSourcePixelCoordinates(sourceGeoCoding._obj, sourceWidth, sourceHeight, destGeoCoding._obj, destArea._obj))

    def computeMinMaxY(pixelPositions):
        return ProductUtils_computeMinMaxY(pixelPositions._obj)

    def copyBandsForGeomTransform(sourceProduct, targetProduct, defaultNoDataValue, addedRasterDataNodes):
        ProductUtils_copyBandsForGeomTransform1(sourceProduct._obj, targetProduct._obj, defaultNoDataValue, addedRasterDataNodes._obj)
        return

    def copyBandsForGeomTransform(sourceProduct, targetProduct, includeTiePointGrids, defaultNoDataValue, targetToSourceMap):
        ProductUtils_copyBandsForGeomTransform2(sourceProduct._obj, targetProduct._obj, includeTiePointGrids, defaultNoDataValue, targetToSourceMap._obj)
        return

    def getScanLineTime(product, y):
        return ProductData_UTC(ProductUtils_getScanLineTime(product._obj, y))

    def getGeophysicalSampleDouble(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleDouble(band._obj, pixelX, pixelY, level)

    def getGeophysicalSampleLong(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleLong(band._obj, pixelX, pixelY, level)


class MetadataElement:
    def __init__(self, obj):
        self._obj = obj

    def newMetadataElement(name):
        return MetadataElement(MetadataElement_newMetadataElement(name))

    def getElementGroup(self):
        return ProductNodeGroup(MetadataElement_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(MetadataElement_getParentElement(self._obj))

    def addElement(self, element):
        MetadataElement_addElement(self._obj, element._obj)
        return

    def addElementAt(self, element, index):
        MetadataElement_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        return MetadataElement_removeElement(self._obj, element._obj)

    def getNumElements(self):
        return MetadataElement_getNumElements(self._obj)

    def getElementAt(self, index):
        return MetadataElement(MetadataElement_getElementAt(self._obj, index))

    def getElementNames(self):
        return MetadataElement_getElementNames(self._obj)

    def getElements(self):
        return MetadataElement(MetadataElement_getElements(self._obj))

    def getElement(self, name):
        return MetadataElement(MetadataElement_getElement(self._obj, name))

    def containsElement(self, name):
        return MetadataElement_containsElement(self._obj, name)

    def getElementIndex(self, element):
        return MetadataElement_getElementIndex(self._obj, element._obj)

    def addAttribute(self, attribute):
        MetadataElement_addAttribute(self._obj, attribute._obj)
        return

    def removeAttribute(self, attribute):
        return MetadataElement_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        return MetadataElement_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        return MetadataAttribute(MetadataElement_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        return MetadataElement_getAttributeNames(self._obj)

    def getAttributes(self):
        return MetadataAttribute(MetadataElement_getAttributes(self._obj))

    def getAttribute(self, name):
        return MetadataAttribute(MetadataElement_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        return MetadataElement_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        return MetadataElement_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        return MetadataElement_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        return ProductData_UTC(MetadataElement_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        return MetadataElement_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        MetadataElement_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        MetadataElement_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        MetadataElement_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        return MetadataElement_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        MetadataElement_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        MetadataElement_setModified(self._obj, modified)
        return

    def acceptVisitor(self, visitor):
        MetadataElement_acceptVisitor(self._obj, visitor._obj)
        return

    def createDeepClone(self):
        return MetadataElement(MetadataElement_createDeepClone(self._obj))

    def dispose(self):
        MetadataElement_dispose(self._obj)
        return

    def getOwner(self):
        return ProductNode(MetadataElement_getOwner(self._obj))

    def getName(self):
        return MetadataElement_getName(self._obj)

    def setName(self, name):
        MetadataElement_setName(self._obj, name)
        return

    def getDescription(self):
        return MetadataElement_getDescription(self._obj)

    def setDescription(self, description):
        MetadataElement_setDescription(self._obj, description)
        return

    def isModified(self):
        return MetadataElement_isModified(self._obj)

    def toString(self):
        return MetadataElement_toString(self._obj)

    def isValidNodeName(name):
        return MetadataElement_isValidNodeName(name)

    def getProduct(self):
        return Product(MetadataElement_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(MetadataElement_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(MetadataElement_getProductWriter(self._obj))

    def getDisplayName(self):
        return MetadataElement_getDisplayName(self._obj)

    def getProductRefString(self):
        return MetadataElement_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        MetadataElement_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        MetadataElement_removeFromFile(self._obj, productWriter._obj)
        return


class Pointing:
    def __init__(self, obj):
        self._obj = obj


class Color:
    def __init__(self, obj):
        self._obj = obj


class PlacemarkDescriptor:
    def __init__(self, obj):
        self._obj = obj


class PointingFactory:
    def __init__(self, obj):
        self._obj = obj


class TransectProfileData:
    def __init__(self, obj):
        self._obj = obj


class PlacemarkGroup:
    def __init__(self, obj):
        self._obj = obj

    def getVectorDataNode(self):
        return VectorDataNode(PlacemarkGroup_getVectorDataNode(self._obj))

    def getPlacemark(self, feature):
        return Placemark(PlacemarkGroup_getPlacemark(self._obj, feature._obj))

    def add(self, placemark):
        return PlacemarkGroup_add4(self._obj, placemark._obj)

    def add(self, index, placemark):
        PlacemarkGroup_add2(self._obj, index, placemark._obj)
        return

    def remove(self, placemark):
        return PlacemarkGroup_remove2(self._obj, placemark._obj)

    def dispose(self):
        PlacemarkGroup_dispose(self._obj)
        return

    def isTakingOverNodeOwnership(self):
        return PlacemarkGroup_isTakingOverNodeOwnership(self._obj)

    def getNodeCount(self):
        return PlacemarkGroup_getNodeCount(self._obj)

    def get(self, index):
        return T(PlacemarkGroup_get1(self._obj, index))

    def getNodeDisplayNames(self):
        return PlacemarkGroup_getNodeDisplayNames(self._obj)

    def getNodeNames(self):
        return PlacemarkGroup_getNodeNames(self._obj)

    def toArray(self):
        return ProductNode(PlacemarkGroup_toArray1(self._obj))

    def toArray(self, array):
        return T(PlacemarkGroup_toArray2(self._obj, array._obj))

    def indexOf(self, name):
        return PlacemarkGroup_indexOf2(self._obj, name)

    def indexOf(self, element):
        return PlacemarkGroup_indexOf1(self._obj, element._obj)

    def getByDisplayName(self, displayName):
        return T(PlacemarkGroup_getByDisplayName(self._obj, displayName))

    def get(self, name):
        return T(PlacemarkGroup_get2(self._obj, name))

    def contains(self, name):
        return PlacemarkGroup_contains2(self._obj, name)

    def contains(self, node):
        return PlacemarkGroup_contains1(self._obj, node._obj)

    def add(self, node):
        return PlacemarkGroup_add3(self._obj, node._obj)

    def add(self, index, node):
        PlacemarkGroup_add1(self._obj, index, node._obj)
        return

    def remove(self, node):
        return PlacemarkGroup_remove1(self._obj, node._obj)

    def removeAll(self):
        PlacemarkGroup_removeAll(self._obj)
        return

    def clearRemovedList(self):
        PlacemarkGroup_clearRemovedList(self._obj)
        return

    def getRemovedNodes(self):
        return Collection(PlacemarkGroup_getRemovedNodes(self._obj))

    def getRawStorageSize(self, subsetDef):
        return PlacemarkGroup_getRawStorageSize2(self._obj, subsetDef._obj)

    def setModified(self, modified):
        PlacemarkGroup_setModified(self._obj, modified)
        return

    def acceptVisitor(self, visitor):
        PlacemarkGroup_acceptVisitor(self._obj, visitor._obj)
        return

    def updateExpression(self, oldExternalName, newExternalName):
        PlacemarkGroup_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def getOwner(self):
        return ProductNode(PlacemarkGroup_getOwner(self._obj))

    def getName(self):
        return PlacemarkGroup_getName(self._obj)

    def setName(self, name):
        PlacemarkGroup_setName(self._obj, name)
        return

    def getDescription(self):
        return PlacemarkGroup_getDescription(self._obj)

    def setDescription(self, description):
        PlacemarkGroup_setDescription(self._obj, description)
        return

    def isModified(self):
        return PlacemarkGroup_isModified(self._obj)

    def toString(self):
        return PlacemarkGroup_toString(self._obj)

    def isValidNodeName(name):
        return PlacemarkGroup_isValidNodeName(name)

    def getProduct(self):
        return Product(PlacemarkGroup_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(PlacemarkGroup_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(PlacemarkGroup_getProductWriter(self._obj))

    def getDisplayName(self):
        return PlacemarkGroup_getDisplayName(self._obj)

    def getProductRefString(self):
        return PlacemarkGroup_getProductRefString(self._obj)

    def getRawStorageSize(self):
        return PlacemarkGroup_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        PlacemarkGroup_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        PlacemarkGroup_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        PlacemarkGroup_removeFromFile(self._obj, productWriter._obj)
        return


class Product:
    def __init__(self, obj):
        self._obj = obj

    def newProduct(name, type, sceneRasterWidth, sceneRasterHeight):
        return Product(Product_newProduct(name, type, sceneRasterWidth, sceneRasterHeight))

    def getFileLocation(self):
        return File(Product_getFileLocation(self._obj))

    def setFileLocation(self, fileLocation):
        Product_setFileLocation(self._obj, fileLocation._obj)
        return

    def getProductType(self):
        return Product_getProductType(self._obj)

    def setProductType(self, productType):
        Product_setProductType(self._obj, productType)
        return

    def setProductReader(self, reader):
        Product_setProductReader(self._obj, reader._obj)
        return

    def getProductReader(self):
        return ProductReader(Product_getProductReader(self._obj))

    def setProductWriter(self, writer):
        Product_setProductWriter(self._obj, writer._obj)
        return

    def getProductWriter(self):
        return ProductWriter(Product_getProductWriter(self._obj))

    def writeHeader(self, output):
        Product_writeHeader(self._obj, output._obj)
        return

    def closeProductReader(self):
        Product_closeProductReader(self._obj)
        return

    def closeProductWriter(self):
        Product_closeProductWriter(self._obj)
        return

    def closeIO(self):
        Product_closeIO(self._obj)
        return

    def dispose(self):
        Product_dispose(self._obj)
        return

    def getPointingFactory(self):
        return PointingFactory(Product_getPointingFactory(self._obj))

    def setPointingFactory(self, pointingFactory):
        Product_setPointingFactory(self._obj, pointingFactory._obj)
        return

    def setGeoCoding(self, geoCoding):
        Product_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getGeoCoding(self):
        return GeoCoding(Product_getGeoCoding(self._obj))

    def isUsingSingleGeoCoding(self):
        return Product_isUsingSingleGeoCoding(self._obj)

    def transferGeoCodingTo(self, destProduct, subsetDef):
        return Product_transferGeoCodingTo(self._obj, destProduct._obj, subsetDef._obj)

    def getSceneRasterWidth(self):
        return Product_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        return Product_getSceneRasterHeight(self._obj)

    def getStartTime(self):
        return ProductData_UTC(Product_getStartTime(self._obj))

    def setStartTime(self, startTime):
        Product_setStartTime(self._obj, startTime._obj)
        return

    def getEndTime(self):
        return ProductData_UTC(Product_getEndTime(self._obj))

    def setEndTime(self, endTime):
        Product_setEndTime(self._obj, endTime._obj)
        return

    def getMetadataRoot(self):
        return MetadataElement(Product_getMetadataRoot(self._obj))

    def getBandGroup(self):
        return ProductNodeGroup(Product_getBandGroup(self._obj))

    def getTiePointGridGroup(self):
        return ProductNodeGroup(Product_getTiePointGridGroup(self._obj))

    def addTiePointGrid(self, tiePointGrid):
        Product_addTiePointGrid(self._obj, tiePointGrid._obj)
        return

    def removeTiePointGrid(self, tiePointGrid):
        return Product_removeTiePointGrid(self._obj, tiePointGrid._obj)

    def getNumTiePointGrids(self):
        return Product_getNumTiePointGrids(self._obj)

    def getTiePointGridAt(self, index):
        return TiePointGrid(Product_getTiePointGridAt(self._obj, index))

    def getTiePointGridNames(self):
        return Product_getTiePointGridNames(self._obj)

    def getTiePointGrids(self):
        return TiePointGrid(Product_getTiePointGrids(self._obj))

    def getTiePointGrid(self, name):
        return TiePointGrid(Product_getTiePointGrid(self._obj, name))

    def containsTiePointGrid(self, name):
        return Product_containsTiePointGrid(self._obj, name)

    def addBand(self, band):
        Product_addBand(self._obj, band._obj)
        return

    def addBand(self, bandName, dataType):
        return Band(Product_addNewBand(self._obj, bandName, dataType))

    def addBand(self, bandName, expression):
        return Band(Product_addComputedBand(self._obj, bandName, expression))

    def removeBand(self, band):
        return Product_removeBand(self._obj, band._obj)

    def getNumBands(self):
        return Product_getNumBands(self._obj)

    def getBandAt(self, index):
        return Band(Product_getBandAt(self._obj, index))

    def getBandNames(self):
        return Product_getBandNames(self._obj)

    def getBands(self):
        return Band(Product_getBands(self._obj))

    def getBand(self, name):
        return Band(Product_getBand(self._obj, name))

    def getBandIndex(self, name):
        return Product_getBandIndex(self._obj, name)

    def containsBand(self, name):
        return Product_containsBand(self._obj, name)

    def containsRasterDataNode(self, name):
        return Product_containsRasterDataNode(self._obj, name)

    def getRasterDataNode(self, name):
        return RasterDataNode(Product_getRasterDataNode(self._obj, name))

    def getMaskGroup(self):
        return ProductNodeGroup(Product_getMaskGroup(self._obj))

    def getVectorDataGroup(self):
        return ProductNodeGroup(Product_getVectorDataGroup(self._obj))

    def getFlagCodingGroup(self):
        return ProductNodeGroup(Product_getFlagCodingGroup(self._obj))

    def getIndexCodingGroup(self):
        return ProductNodeGroup(Product_getIndexCodingGroup(self._obj))

    def containsPixel(self, x, y):
        return Product_containsPixel(self._obj, x, y)

    def getGcpGroup(self):
        return PlacemarkGroup(Product_getGcpGroup(self._obj))

    def getPinGroup(self):
        return PlacemarkGroup(Product_getPinGroup(self._obj))

    def isCompatibleProduct(self, product, eps):
        return Product_isCompatibleProduct(self._obj, product._obj, eps)

    def parseExpression(self, expression):
        return Term(Product_parseExpression(self._obj, expression))

    def acceptVisitor(self, visitor):
        Product_acceptVisitor(self._obj, visitor._obj)
        return

    def addProductNodeListener(self, listener):
        return Product_addProductNodeListener(self._obj, listener._obj)

    def removeProductNodeListener(self, listener):
        Product_removeProductNodeListener(self._obj, listener._obj)
        return

    def getProductNodeListeners(self):
        return ProductNodeListener(Product_getProductNodeListeners(self._obj))

    def getRefNo(self):
        return Product_getRefNo(self._obj)

    def setRefNo(self, refNo):
        Product_setRefNo(self._obj, refNo)
        return

    def resetRefNo(self):
        Product_resetRefNo(self._obj)
        return

    def getProductManager(self):
        return ProductManager(Product_getProductManager(self._obj))

    def createBandArithmeticParser(self):
        return Parser(Product_createBandArithmeticParser(self._obj))

    def createBandArithmeticDefaultNamespace(self):
        return WritableNamespace(Product_createBandArithmeticDefaultNamespace(self._obj))

    def createSubset(self, subsetDef, name, desc):
        return Product(Product_createSubset(self._obj, subsetDef._obj, name, desc))

    def createProjectedProduct(self, mapInfo, name, desc):
        return Product(Product_createProjectedProduct(self._obj, mapInfo._obj, name, desc))

    def createFlippedProduct(self, flipType, name, desc):
        return Product(Product_createFlippedProduct(self._obj, flipType, name, desc))

    def setModified(self, modified):
        Product_setModified(self._obj, modified)
        return

    def getQuicklookBandName(self):
        return Product_getQuicklookBandName(self._obj)

    def setQuicklookBandName(self, quicklookBandName):
        Product_setQuicklookBandName(self._obj, quicklookBandName)
        return

    def createPixelInfoString(self, pixelX, pixelY):
        return Product_createPixelInfoString(self._obj, pixelX, pixelY)

    def getRemovedChildNodes(self):
        return ProductNode(Product_getRemovedChildNodes(self._obj))

    def canBeOrthorectified(self):
        return Product_canBeOrthorectified(self._obj)

    def getPreferredTileSize(self):
        return Dimension(Product_getPreferredTileSize(self._obj))

    def setPreferredTileSize(self, tileWidth, tileHeight):
        Product_setPreferredTileSize(self._obj, tileWidth, tileHeight)
        return

    def getAllFlagNames(self):
        return Product_getAllFlagNames(self._obj)

    def getAutoGrouping(self):
        return Product_AutoGrouping(Product_getAutoGrouping(self._obj))

    def setAutoGrouping(self, pattern):
        Product_setAutoGrouping(self._obj, pattern)
        return

    def addMask(self, maskName, expression, description, color, transparency):
        return Mask(Product_addComputedMask(self._obj, maskName, expression, description, color._obj, transparency))

    def getOwner(self):
        return ProductNode(Product_getOwner(self._obj))

    def getName(self):
        return Product_getName(self._obj)

    def setName(self, name):
        Product_setName(self._obj, name)
        return

    def getDescription(self):
        return Product_getDescription(self._obj)

    def setDescription(self, description):
        Product_setDescription(self._obj, description)
        return

    def isModified(self):
        return Product_isModified(self._obj)

    def toString(self):
        return Product_toString(self._obj)

    def isValidNodeName(name):
        return Product_isValidNodeName(name)

    def getProduct(self):
        return Product(Product_getProduct(self._obj))

    def getDisplayName(self):
        return Product_getDisplayName(self._obj)

    def getProductRefString(self):
        return Product_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        Product_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        Product_removeFromFile(self._obj, productWriter._obj)
        return


class Point2D:
    def __init__(self, obj):
        self._obj = obj


class T:
    def __init__(self, obj):
        self._obj = obj


class ProductVisitor:
    def __init__(self, obj):
        self._obj = obj


class Scaling:
    def __init__(self, obj):
        self._obj = obj


class WritableNamespace:
    def __init__(self, obj):
        self._obj = obj


class MultiLevelImage:
    def __init__(self, obj):
        self._obj = obj


class ROI:
    def __init__(self, obj):
        self._obj = obj


class Collection:
    def __init__(self, obj):
        self._obj = obj


class ProductManager_Listener:
    def __init__(self, obj):
        self._obj = obj


class GeoTIFFMetadata:
    def __init__(self, obj):
        self._obj = obj


class ColorPaletteDef:
    def __init__(self, obj):
        self._obj = obj

    def newColorPaletteDef(minSample, maxSample):
        return ColorPaletteDef(ColorPaletteDef_newColorPaletteDefFromRange(minSample, maxSample))

    def newColorPaletteDef(points, numColors):
        return ColorPaletteDef(ColorPaletteDef_newColorPaletteDefFromPoints(points._obj, numColors))

    def isDiscrete(self):
        return ColorPaletteDef_isDiscrete(self._obj)

    def setDiscrete(self, discrete):
        ColorPaletteDef_setDiscrete(self._obj, discrete)
        return

    def getNumColors(self):
        return ColorPaletteDef_getNumColors(self._obj)

    def setNumColors(self, numColors):
        ColorPaletteDef_setNumColors(self._obj, numColors)
        return

    def getNumPoints(self):
        return ColorPaletteDef_getNumPoints(self._obj)

    def setNumPoints(self, numPoints):
        ColorPaletteDef_setNumPoints(self._obj, numPoints)
        return

    def isAutoDistribute(self):
        return ColorPaletteDef_isAutoDistribute(self._obj)

    def setAutoDistribute(self, autoDistribute):
        ColorPaletteDef_setAutoDistribute(self._obj, autoDistribute)
        return

    def getPointAt(self, index):
        return ColorPaletteDef_Point(ColorPaletteDef_getPointAt(self._obj, index))

    def getFirstPoint(self):
        return ColorPaletteDef_Point(ColorPaletteDef_getFirstPoint(self._obj))

    def getLastPoint(self):
        return ColorPaletteDef_Point(ColorPaletteDef_getLastPoint(self._obj))

    def getMinDisplaySample(self):
        return ColorPaletteDef_getMinDisplaySample(self._obj)

    def getMaxDisplaySample(self):
        return ColorPaletteDef_getMaxDisplaySample(self._obj)

    def insertPointAfter(self, index, point):
        ColorPaletteDef_insertPointAfter(self._obj, index, point._obj)
        return

    def createPointAfter(self, index, scaling):
        return ColorPaletteDef_createPointAfter(self._obj, index, scaling._obj)

    def getCenterColor(c1, c2):
        return Color(ColorPaletteDef_getCenterColor(c1._obj, c2._obj))

    def removePointAt(self, index):
        ColorPaletteDef_removePointAt(self._obj, index)
        return

    def addPoint(self, point):
        ColorPaletteDef_addPoint(self._obj, point._obj)
        return

    def getPoints(self):
        return ColorPaletteDef_Point(ColorPaletteDef_getPoints(self._obj))

    def setPoints(self, points):
        ColorPaletteDef_setPoints(self._obj, points._obj)
        return

    def getIterator(self):
        return Iterator(ColorPaletteDef_getIterator(self._obj))

    def clone(self):
        return Object(ColorPaletteDef_clone(self._obj))

    def createDeepCopy(self):
        return ColorPaletteDef(ColorPaletteDef_createDeepCopy(self._obj))

    def loadColorPaletteDef(file):
        return ColorPaletteDef(ColorPaletteDef_loadColorPaletteDef(file._obj))

    def storeColorPaletteDef(colorPaletteDef, file):
        ColorPaletteDef_storeColorPaletteDef(colorPaletteDef._obj, file._obj)
        return

    def dispose(self):
        ColorPaletteDef_dispose(self._obj)
        return

    def getColors(self):
        return Color(ColorPaletteDef_getColors(self._obj))

    def createColorPalette(self, scaling):
        return Color(ColorPaletteDef_createColorPalette(self._obj, scaling._obj))

    def computeColor(self, scaling, sample):
        return Color(ColorPaletteDef_computeColor(self._obj, scaling._obj, sample))


class MapInfo:
    def __init__(self, obj):
        self._obj = obj


class ImageInfo:
    def __init__(self, obj):
        self._obj = obj

    def newImageInfo(colorPaletteDef):
        return ImageInfo(ImageInfo_newImageInfoPalette(colorPaletteDef._obj))

    def newImageInfo(rgbChannelDef):
        return ImageInfo(ImageInfo_newImageInfoRGB(rgbChannelDef._obj))

    def getColorPaletteDef(self):
        return ColorPaletteDef(ImageInfo_getColorPaletteDef(self._obj))

    def getRgbChannelDef(self):
        return RGBChannelDef(ImageInfo_getRgbChannelDef(self._obj))

    def getNoDataColor(self):
        return Color(ImageInfo_getNoDataColor(self._obj))

    def setNoDataColor(self, noDataColor):
        ImageInfo_setNoDataColor(self._obj, noDataColor._obj)
        return

    def setHistogramMatching(self, histogramMatching):
        ImageInfo_setHistogramMatching(self._obj, histogramMatching._obj)
        return

    def isLogScaled(self):
        return ImageInfo_isLogScaled(self._obj)

    def setLogScaled(self, logScaled):
        ImageInfo_setLogScaled(self._obj, logScaled)
        return

    def getColors(self):
        return Color(ImageInfo_getColors(self._obj))

    def getColorComponentCount(self):
        return ImageInfo_getColorComponentCount(self._obj)

    def createIndexColorModel(self, scaling):
        return IndexColorModel(ImageInfo_createIndexColorModel(self._obj, scaling._obj))

    def createComponentColorModel(self):
        return ComponentColorModel(ImageInfo_createComponentColorModel(self._obj))

    def clone(self):
        return Object(ImageInfo_clone(self._obj))

    def createDeepCopy(self):
        return ImageInfo(ImageInfo_createDeepCopy(self._obj))

    def dispose(self):
        ImageInfo_dispose(self._obj)
        return

    def setColors(self, colors):
        ImageInfo_setColors(self._obj, colors._obj)
        return

    def setColorPaletteDef(self, colorPaletteDef, minSample, maxSample, autoDistribute):
        ImageInfo_setColorPaletteDef(self._obj, colorPaletteDef._obj, minSample, maxSample, autoDistribute)
        return

    def getHistogramMatching(mode):
        return ImageInfo_HistogramMatching(ImageInfo_getHistogramMatching(mode))


class Histogram:
    def __init__(self, obj):
        self._obj = obj


class String:
    def __init__(self, obj):
        self._obj = obj


class BufferedImage:
    def __init__(self, obj):
        self._obj = obj


class RGBChannelDef:
    def __init__(self, obj):
        self._obj = obj

    def newRGBChannelDef():
        return RGBChannelDef(RGBChannelDef_newRGBChannelDef())

    def getSourceName(self, index):
        return RGBChannelDef_getSourceName(self._obj, index)

    def setSourceName(self, index, sourceName):
        RGBChannelDef_setSourceName(self._obj, index, sourceName)
        return

    def getSourceNames(self):
        return RGBChannelDef_getSourceNames(self._obj)

    def setSourceNames(self, bandNames):
        RGBChannelDef_setSourceNames(self._obj, bandNames)
        return

    def isAlphaUsed(self):
        return RGBChannelDef_isAlphaUsed(self._obj)

    def isGammaUsed(self, index):
        return RGBChannelDef_isGammaUsed(self._obj, index)

    def getGamma(self, index):
        return RGBChannelDef_getGamma(self._obj, index)

    def setGamma(self, index, gamma):
        RGBChannelDef_setGamma(self._obj, index, gamma)
        return

    def getMinDisplaySample(self, index):
        return RGBChannelDef_getMinDisplaySample(self._obj, index)

    def setMinDisplaySample(self, index, min):
        RGBChannelDef_setMinDisplaySample(self._obj, index, min)
        return

    def getMaxDisplaySample(self, index):
        return RGBChannelDef_getMaxDisplaySample(self._obj, index)

    def setMaxDisplaySample(self, index, max):
        RGBChannelDef_setMaxDisplaySample(self._obj, index, max)
        return

    def clone(self):
        return Object(RGBChannelDef_clone(self._obj))


class TiePointGrid:
    def __init__(self, obj):
        self._obj = obj

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints):
        return TiePointGrid(TiePointGrid_newTiePointGrid1(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints))

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity):
        return TiePointGrid(TiePointGrid_newTiePointGrid2(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity))

    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles):
        return TiePointGrid(TiePointGrid_newTiePointGrid3(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles))

    def getDiscontinuity(tiePoints):
        return TiePointGrid_getDiscontinuity2(tiePoints)

    def getDiscontinuity(self):
        return TiePointGrid_getDiscontinuity1(self._obj)

    def setDiscontinuity(self, discontinuity):
        TiePointGrid_setDiscontinuity(self._obj, discontinuity)
        return

    def isFloatingPointType(self):
        return TiePointGrid_isFloatingPointType(self._obj)

    def getGeophysicalDataType(self):
        return TiePointGrid_getGeophysicalDataType(self._obj)

    def getSceneRasterData(self):
        return ProductData(TiePointGrid_getSceneRasterData(self._obj))

    def getSceneRasterWidth(self):
        return TiePointGrid_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        return TiePointGrid_getSceneRasterHeight(self._obj)

    def getOffsetX(self):
        return TiePointGrid_getOffsetX(self._obj)

    def getOffsetY(self):
        return TiePointGrid_getOffsetY(self._obj)

    def getSubSamplingX(self):
        return TiePointGrid_getSubSamplingX(self._obj)

    def getSubSamplingY(self):
        return TiePointGrid_getSubSamplingY(self._obj)

    def getTiePoints(self):
        return TiePointGrid_getTiePoints(self._obj)

    def getPixelInt(self, x, y):
        return TiePointGrid_getPixelInt(self._obj, x, y)

    def dispose(self):
        TiePointGrid_dispose(self._obj)
        return

    def getPixelFloat(self, x, y):
        return TiePointGrid_getPixelFloat2(self._obj, x, y)

    def getPixelFloat(self, x, y):
        return TiePointGrid_getPixelFloat1(self._obj, x, y)

    def getPixelDouble(self, x, y):
        return TiePointGrid_getPixelDouble(self._obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        TiePointGrid_setPixelInt(self._obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        TiePointGrid_setPixelFloat(self._obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        TiePointGrid_setPixelDouble(self._obj, x, y, pixelValue)
        return

    def getPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_getPixels6(self._obj, x, y, w, h, pixels, pm._obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_getPixels4(self._obj, x, y, w, h, pixels, pm._obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_getPixels2(self._obj, x, y, w, h, pixels, pm._obj)

    def setPixels(self, x, y, w, h, pixels):
        TiePointGrid_setPixels3(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        TiePointGrid_setPixels2(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        TiePointGrid_setPixels1(self._obj, x, y, w, h, pixels)
        return

    def readPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_readPixels6(self._obj, x, y, w, h, pixels, pm._obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_readPixels4(self._obj, x, y, w, h, pixels, pm._obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        return TiePointGrid_readPixels2(self._obj, x, y, w, h, pixels, pm._obj)

    def writePixels(self, x, y, w, h, pixels, pm):
        TiePointGrid_writePixels6(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        TiePointGrid_writePixels4(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        TiePointGrid_writePixels2(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        TiePointGrid_readRasterData2(self._obj, offsetX, offsetY, width, height, rasterData._obj, pm._obj)
        return

    def readRasterDataFully(self, pm):
        TiePointGrid_readRasterDataFully2(self._obj, pm._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        TiePointGrid_writeRasterData2(self._obj, offsetX, offsetY, width, height, rasterData._obj, pm._obj)
        return

    def writeRasterDataFully(self, pm):
        TiePointGrid_writeRasterDataFully2(self._obj, pm._obj)
        return

    def acceptVisitor(self, visitor):
        TiePointGrid_acceptVisitor(self._obj, visitor._obj)
        return

    def cloneTiePointGrid(self):
        return TiePointGrid(TiePointGrid_cloneTiePointGrid(self._obj))

    def createZenithFromElevationAngleTiePointGrid(elevationAngleGrid):
        return TiePointGrid(TiePointGrid_createZenithFromElevationAngleTiePointGrid(elevationAngleGrid._obj))

    def createSubset(sourceTiePointGrid, subsetDef):
        return TiePointGrid(TiePointGrid_createSubset(sourceTiePointGrid._obj, subsetDef._obj))

    def getRasterWidth(self):
        return TiePointGrid_getRasterWidth(self._obj)

    def getRasterHeight(self):
        return TiePointGrid_getRasterHeight(self._obj)

    def setModified(self, modified):
        TiePointGrid_setModified(self._obj, modified)
        return

    def getGeoCoding(self):
        return GeoCoding(TiePointGrid_getGeoCoding(self._obj))

    def setGeoCoding(self, geoCoding):
        TiePointGrid_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getPointing(self):
        return Pointing(TiePointGrid_getPointing(self._obj))

    def canBeOrthorectified(self):
        return TiePointGrid_canBeOrthorectified(self._obj)

    def getScalingFactor(self):
        return TiePointGrid_getScalingFactor(self._obj)

    def setScalingFactor(self, scalingFactor):
        TiePointGrid_setScalingFactor(self._obj, scalingFactor)
        return

    def getScalingOffset(self):
        return TiePointGrid_getScalingOffset(self._obj)

    def setScalingOffset(self, scalingOffset):
        TiePointGrid_setScalingOffset(self._obj, scalingOffset)
        return

    def isLog10Scaled(self):
        return TiePointGrid_isLog10Scaled(self._obj)

    def setLog10Scaled(self, log10Scaled):
        TiePointGrid_setLog10Scaled(self._obj, log10Scaled)
        return

    def isScalingApplied(self):
        return TiePointGrid_isScalingApplied(self._obj)

    def isValidMaskProperty(propertyName):
        return TiePointGrid_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        return TiePointGrid_isNoDataValueSet(self._obj)

    def clearNoDataValue(self):
        TiePointGrid_clearNoDataValue(self._obj)
        return

    def isNoDataValueUsed(self):
        return TiePointGrid_isNoDataValueUsed(self._obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        TiePointGrid_setNoDataValueUsed(self._obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        return TiePointGrid_getNoDataValue(self._obj)

    def setNoDataValue(self, noDataValue):
        TiePointGrid_setNoDataValue(self._obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        return TiePointGrid_getGeophysicalNoDataValue(self._obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        TiePointGrid_setGeophysicalNoDataValue(self._obj, noDataValue)
        return

    def getValidPixelExpression(self):
        return TiePointGrid_getValidPixelExpression(self._obj)

    def setValidPixelExpression(self, validPixelExpression):
        TiePointGrid_setValidPixelExpression(self._obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        return TiePointGrid_isValidMaskUsed(self._obj)

    def resetValidMask(self):
        TiePointGrid_resetValidMask(self._obj)
        return

    def getValidMaskExpression(self):
        return TiePointGrid_getValidMaskExpression(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        TiePointGrid_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        return TiePointGrid_hasRasterData(self._obj)

    def getRasterData(self):
        return ProductData(TiePointGrid_getRasterData(self._obj))

    def setRasterData(self, rasterData):
        TiePointGrid_setRasterData(self._obj, rasterData._obj)
        return

    def loadRasterData(self):
        TiePointGrid_loadRasterData1(self._obj)
        return

    def loadRasterData(self, pm):
        TiePointGrid_loadRasterData2(self._obj, pm._obj)
        return

    def unloadRasterData(self):
        TiePointGrid_unloadRasterData(self._obj)
        return

    def isPixelValid(self, x, y):
        return TiePointGrid_isPixelValid2(self._obj, x, y)

    def getSampleInt(self, x, y):
        return TiePointGrid_getSampleInt(self._obj, x, y)

    def getSampleFloat(self, x, y):
        return TiePointGrid_getSampleFloat(self._obj, x, y)

    def isPixelValid(self, pixelIndex):
        return TiePointGrid_isPixelValid1(self._obj, pixelIndex)

    def isPixelValid(self, x, y, roi):
        return TiePointGrid_isPixelValid3(self._obj, x, y, roi._obj)

    def getPixels(self, x, y, w, h, pixels):
        return TiePointGrid_getPixels5(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return TiePointGrid_getPixels3(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        return TiePointGrid_getPixels1(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return TiePointGrid_readPixels5(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return TiePointGrid_readPixels3(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        return TiePointGrid_readPixels1(self._obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        TiePointGrid_writePixels5(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        TiePointGrid_writePixels3(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        TiePointGrid_writePixels1(self._obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return TiePointGrid_readValidMask(self._obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        TiePointGrid_readRasterDataFully1(self._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        TiePointGrid_readRasterData1(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def writeRasterDataFully(self):
        TiePointGrid_writeRasterDataFully1(self._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        TiePointGrid_writeRasterData1(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def createCompatibleRasterData(self):
        return ProductData(TiePointGrid_createCompatibleRasterData1(self._obj))

    def createCompatibleSceneRasterData(self):
        return ProductData(TiePointGrid_createCompatibleSceneRasterData(self._obj))

    def createCompatibleRasterData(self, width, height):
        return ProductData(TiePointGrid_createCompatibleRasterData2(self._obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        return TiePointGrid_isCompatibleRasterData(self._obj, rasterData._obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        TiePointGrid_checkCompatibleRasterData(self._obj, rasterData._obj, w, h)
        return

    def hasIntPixels(self):
        return TiePointGrid_hasIntPixels(self._obj)

    def createTransectProfileData(self, shape):
        return TransectProfileData(TiePointGrid_createTransectProfileData(self._obj, shape._obj))

    def getImageInfo(self):
        return ImageInfo(TiePointGrid_getImageInfo1(self._obj))

    def setImageInfo(self, imageInfo):
        TiePointGrid_setImageInfo(self._obj, imageInfo._obj)
        return

    def fireImageInfoChanged(self):
        TiePointGrid_fireImageInfoChanged(self._obj)
        return

    def getImageInfo(self, pm):
        return ImageInfo(TiePointGrid_getImageInfo2(self._obj, pm._obj))

    def getImageInfo(self, histoSkipAreas, pm):
        return ImageInfo(TiePointGrid_getImageInfo3(self._obj, histoSkipAreas, pm._obj))

    def createDefaultImageInfo(self, histoSkipAreas, pm):
        return ImageInfo(TiePointGrid_createDefaultImageInfo1(self._obj, histoSkipAreas, pm._obj))

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        return ImageInfo(TiePointGrid_createDefaultImageInfo2(self._obj, histoSkipAreas, histogram._obj))

    def getOverlayMaskGroup(self):
        return ProductNodeGroup(TiePointGrid_getOverlayMaskGroup(self._obj))

    def createColorIndexedImage(self, pm):
        return BufferedImage(TiePointGrid_createColorIndexedImage(self._obj, pm._obj))

    def createRgbImage(self, pm):
        return BufferedImage(TiePointGrid_createRgbImage(self._obj, pm._obj))

    def quantizeRasterData(self, newMin, newMax, gamma, pm):
        return TiePointGrid_quantizeRasterData1(self._obj, newMin, newMax, gamma, pm._obj)

    def quantizeRasterData(self, newMin, newMax, gamma, samples, offset, stride, pm):
        TiePointGrid_quantizeRasterData2(self._obj, newMin, newMax, gamma, samples, offset, stride, pm._obj)
        return

    def createPixelValidator(self, lineOffset, roi):
        return IndexValidator(TiePointGrid_createPixelValidator(self._obj, lineOffset, roi._obj))

    def scale(self, v):
        return TiePointGrid_scale(self._obj, v)

    def scaleInverse(self, v):
        return TiePointGrid_scaleInverse(self._obj, v)

    def getPixelString(self, x, y):
        return TiePointGrid_getPixelString(self._obj, x, y)

    def isSourceImageSet(self):
        return TiePointGrid_isSourceImageSet(self._obj)

    def getSourceImage(self):
        return MultiLevelImage(TiePointGrid_getSourceImage(self._obj))

    def setSourceImage(self, sourceImage):
        TiePointGrid_setSourceImage2(self._obj, sourceImage._obj)
        return

    def setSourceImage(self, sourceImage):
        TiePointGrid_setSourceImage1(self._obj, sourceImage._obj)
        return

    def isGeophysicalImageSet(self):
        return TiePointGrid_isGeophysicalImageSet(self._obj)

    def getGeophysicalImage(self):
        return MultiLevelImage(TiePointGrid_getGeophysicalImage(self._obj))

    def isValidMaskImageSet(self):
        return TiePointGrid_isValidMaskImageSet(self._obj)

    def getValidMaskImage(self):
        return MultiLevelImage(TiePointGrid_getValidMaskImage(self._obj))

    def isStxSet(self):
        return TiePointGrid_isStxSet(self._obj)

    def getStx(self):
        return Stx(TiePointGrid_getStx1(self._obj))

    def getStx(self, accurate, pm):
        return Stx(TiePointGrid_getStx2(self._obj, accurate, pm._obj))

    def setStx(self, stx):
        TiePointGrid_setStx(self._obj, stx._obj)
        return

    def getValidShape(self):
        return Shape(TiePointGrid_getValidShape(self._obj))

    def getDataType(self):
        return TiePointGrid_getDataType(self._obj)

    def getNumDataElems(self):
        return TiePointGrid_getNumDataElems(self._obj)

    def setData(self, data):
        TiePointGrid_setData(self._obj, data._obj)
        return

    def getData(self):
        return ProductData(TiePointGrid_getData(self._obj))

    def setDataElems(self, elems):
        TiePointGrid_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        return Object(TiePointGrid_getDataElems(self._obj))

    def getDataElemSize(self):
        return TiePointGrid_getDataElemSize(self._obj)

    def setReadOnly(self, readOnly):
        TiePointGrid_setReadOnly(self._obj, readOnly)
        return

    def isReadOnly(self):
        return TiePointGrid_isReadOnly(self._obj)

    def setUnit(self, unit):
        TiePointGrid_setUnit(self._obj, unit)
        return

    def getUnit(self):
        return TiePointGrid_getUnit(self._obj)

    def fireProductNodeDataChanged(self):
        TiePointGrid_fireProductNodeDataChanged(self._obj)
        return

    def getRawStorageSize(self, subsetDef):
        return TiePointGrid_getRawStorageSize2(self._obj, subsetDef._obj)

    def createCompatibleProductData(self, numElems):
        return ProductData(TiePointGrid_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        return ProductNode(TiePointGrid_getOwner(self._obj))

    def getName(self):
        return TiePointGrid_getName(self._obj)

    def setName(self, name):
        TiePointGrid_setName(self._obj, name)
        return

    def getDescription(self):
        return TiePointGrid_getDescription(self._obj)

    def setDescription(self, description):
        TiePointGrid_setDescription(self._obj, description)
        return

    def isModified(self):
        return TiePointGrid_isModified(self._obj)

    def toString(self):
        return TiePointGrid_toString(self._obj)

    def isValidNodeName(name):
        return TiePointGrid_isValidNodeName(name)

    def getProduct(self):
        return Product(TiePointGrid_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(TiePointGrid_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(TiePointGrid_getProductWriter(self._obj))

    def getDisplayName(self):
        return TiePointGrid_getDisplayName(self._obj)

    def getProductRefString(self):
        return TiePointGrid_getProductRefString(self._obj)

    def getRawStorageSize(self):
        return TiePointGrid_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        TiePointGrid_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        TiePointGrid_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        TiePointGrid_removeFromFile(self._obj, productWriter._obj)
        return


class SimpleFeature:
    def __init__(self, obj):
        self._obj = obj


class ProductSubsetDef:
    def __init__(self, obj):
        self._obj = obj


class ProductWriter:
    def __init__(self, obj):
        self._obj = obj

    def getWriterPlugIn(self):
        return ProductWriterPlugIn(ProductWriter_getWriterPlugIn(self._obj))

    def getOutput(self):
        return Object(ProductWriter_getOutput(self._obj))

    def writeProductNodes(self, product, output):
        ProductWriter_writeProductNodes(self._obj, product._obj, output._obj)
        return

    def writeBandRasterData(self, sourceBand, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer, pm):
        ProductWriter_writeBandRasterData(self._obj, sourceBand._obj, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer._obj, pm._obj)
        return

    def flush(self):
        ProductWriter_flush(self._obj)
        return

    def close(self):
        ProductWriter_close(self._obj)
        return

    def shouldWrite(self, node):
        return ProductWriter_shouldWrite(self._obj, node._obj)

    def isIncrementalMode(self):
        return ProductWriter_isIncrementalMode(self._obj)

    def setIncrementalMode(self, enabled):
        ProductWriter_setIncrementalMode(self._obj, enabled)
        return

    def deleteOutput(self):
        ProductWriter_deleteOutput(self._obj)
        return

    def removeBand(self, band):
        ProductWriter_removeBand(self._obj, band._obj)
        return


class MetadataAttribute:
    def __init__(self, obj):
        self._obj = obj

    def newMetadataAttribute(name, data, readOnly):
        return MetadataAttribute(MetadataAttribute_newMetadataAttribute(name, data._obj, readOnly))

    def getParentElement(self):
        return MetadataElement(MetadataAttribute_getParentElement(self._obj))

    def equals(self, object):
        return MetadataAttribute_equals(self._obj, object._obj)

    def acceptVisitor(self, visitor):
        MetadataAttribute_acceptVisitor(self._obj, visitor._obj)
        return

    def createDeepClone(self):
        return MetadataAttribute(MetadataAttribute_createDeepClone(self._obj))

    def getDataType(self):
        return MetadataAttribute_getDataType(self._obj)

    def isFloatingPointType(self):
        return MetadataAttribute_isFloatingPointType(self._obj)

    def getNumDataElems(self):
        return MetadataAttribute_getNumDataElems(self._obj)

    def setData(self, data):
        MetadataAttribute_setData(self._obj, data._obj)
        return

    def getData(self):
        return ProductData(MetadataAttribute_getData(self._obj))

    def setDataElems(self, elems):
        MetadataAttribute_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        return Object(MetadataAttribute_getDataElems(self._obj))

    def getDataElemSize(self):
        return MetadataAttribute_getDataElemSize(self._obj)

    def setReadOnly(self, readOnly):
        MetadataAttribute_setReadOnly(self._obj, readOnly)
        return

    def isReadOnly(self):
        return MetadataAttribute_isReadOnly(self._obj)

    def setUnit(self, unit):
        MetadataAttribute_setUnit(self._obj, unit)
        return

    def getUnit(self):
        return MetadataAttribute_getUnit(self._obj)

    def fireProductNodeDataChanged(self):
        MetadataAttribute_fireProductNodeDataChanged(self._obj)
        return

    def dispose(self):
        MetadataAttribute_dispose(self._obj)
        return

    def createCompatibleProductData(self, numElems):
        return ProductData(MetadataAttribute_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        return ProductNode(MetadataAttribute_getOwner(self._obj))

    def getName(self):
        return MetadataAttribute_getName(self._obj)

    def setName(self, name):
        MetadataAttribute_setName(self._obj, name)
        return

    def getDescription(self):
        return MetadataAttribute_getDescription(self._obj)

    def setDescription(self, description):
        MetadataAttribute_setDescription(self._obj, description)
        return

    def isModified(self):
        return MetadataAttribute_isModified(self._obj)

    def setModified(self, modified):
        MetadataAttribute_setModified(self._obj, modified)
        return

    def toString(self):
        return MetadataAttribute_toString(self._obj)

    def isValidNodeName(name):
        return MetadataAttribute_isValidNodeName(name)

    def getProduct(self):
        return Product(MetadataAttribute_getProduct(self._obj))

    def getProductReader(self):
        return ProductReader(MetadataAttribute_getProductReader(self._obj))

    def getProductWriter(self):
        return ProductWriter(MetadataAttribute_getProductWriter(self._obj))

    def getDisplayName(self):
        return MetadataAttribute_getDisplayName(self._obj)

    def getProductRefString(self):
        return MetadataAttribute_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        MetadataAttribute_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        MetadataAttribute_removeFromFile(self._obj, productWriter._obj)
        return


class ProgressMonitor:
    def __init__(self, obj):
        self._obj = obj


class VectorDataNode:
    def __init__(self, obj):
        self._obj = obj


class GeneralPath:
    def __init__(self, obj):
        self._obj = obj


class ImageInputStream:
    def __init__(self, obj):
        self._obj = obj


