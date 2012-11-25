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

    @staticmethod
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

    @staticmethod
    def calculateEastingNorthing(sourceProduct, targetCrs, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY):
        return Point2D(ImageGeometry_calculateEastingNorthing(sourceProduct._obj, targetCrs._obj, referencePixelX, referencePixelY, pixelSizeX, pixelSizeY))

    @staticmethod
    def calculateProductSize(sourceProduct, targetCrs, pixelSizeX, pixelSizeY):
        return Rectangle(ImageGeometry_calculateProductSize(sourceProduct._obj, targetCrs._obj, pixelSizeX, pixelSizeY))

    @staticmethod
    def createTargetGeometry(sourceProduct, targetCrs, pixelSizeX, pixelSizeY, width, height, orientation, easting, northing, referencePixelX, referencePixelY):
        return ImageGeometry(ImageGeometry_createTargetGeometry(sourceProduct._obj, targetCrs._obj, pixelSizeX._obj, pixelSizeY._obj, width._obj, height._obj, orientation._obj, easting._obj, northing._obj, referencePixelX._obj, referencePixelY._obj))

    @staticmethod
    def createCollocationTargetGeometry(targetProduct, collocationProduct):
        return ImageGeometry(ImageGeometry_createCollocationTargetGeometry(targetProduct._obj, collocationProduct._obj))


"""
The <code>GeoCoding</code> interface provides geo-spatial latitude and longitude information for a given X/Y position
of any (two-dimensional) raster.
 <b> Note: New geo-coding implementations shall implement the abstract class {@link AbstractGeoCoding},
instead of implementing this interface.</b>


All <code>GeoCoding</code> implementations should override
the {@link Object#equals(Object) equals()} and  {@link Object#hashCode() hashCode()} methods.
"""
class GeoCoding:
    def __init__(self, obj):
        self._obj = obj

    def isCrossingMeridianAt180(self):
        """
           Checks whether or not the longitudes of this geo-coding cross the +/- 180 degree meridian.
           @return <code>true</code>, if so
        """
        return GeoCoding_isCrossingMeridianAt180(self._obj)

    def canGetPixelPos(self):
        """
           Checks whether or not this geo-coding can determine the pixel position from a geodetic position.
           @return <code>true</code>, if so
        """
        return GeoCoding_canGetPixelPos(self._obj)

    def canGetGeoPos(self):
        """
           Checks whether or not this geo-coding can determine the geodetic position from a pixel position.
           @return <code>true</code>, if so
        """
        return GeoCoding_canGetGeoPos(self._obj)

    def getPixelPos(self, geoPos, pixelPos):
        """
           Returns the pixel co-ordinates as x/y for a given geographical position given as lat/lon.
           @param geoPos   the geographical position as lat/lon in the coodinate system determined by {@link #getDatum()}
           @param pixelPos an instance of <code>Point</code> to be used as retun value. If this parameter is
           <code>null</code>, the method creates a new instance which it then returns.
           @return the pixel co-ordinates as x/y
        """
        return PixelPos(GeoCoding_getPixelPos(self._obj, geoPos._obj, pixelPos._obj))

    def getGeoPos(self, pixelPos, geoPos):
        """
           Returns the latitude and longitude value for a given pixel co-ordinate.
           @param pixelPos the pixel's co-ordinates given as x,y
           @param geoPos   an instance of <code>GeoPos</code> to be used as retun value. If this parameter is
           <code>null</code>, the method creates a new instance which it then returns.
           @return the geographical position as lat/lon in the coodinate system determined by {@link #getDatum()}
        """
        return GeoPos(GeoCoding_getGeoPos(self._obj, pixelPos._obj, geoPos._obj))

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
        """
        GeoCoding_dispose(self._obj)
        return

    def getImageCRS(self):
        """
           @return The image coordinate reference system (CRS). It is usually derived from the base CRS by including
           a linear or non-linear transformation from base (geodetic) coordinates to image coordinates.
        """
        return CoordinateReferenceSystem(GeoCoding_getImageCRS(self._obj))

    def getMapCRS(self):
        """
           @return The map coordinate reference system (CRS). It may be either a geographical CRS (nominal case is
           "WGS-84") or a derived projected CRS, e.g. "UTM 32 - North".
        """
        return CoordinateReferenceSystem(GeoCoding_getMapCRS(self._obj))

    def getGeoCRS(self):
        """
           @return The geographical coordinate reference system (CRS). It may be either "WGS-84" (nominal case) or
           any other geographical CRS.
        """
        return CoordinateReferenceSystem(GeoCoding_getGeoCRS(self._obj))

    def getImageToMapTransform(self):
        """
           @return The math transformation used to convert image coordinates to map coordinates.
        """
        return MathTransform(GeoCoding_getImageToMapTransform(self._obj))


class Parser:
    def __init__(self, obj):
        self._obj = obj


"""
The abstract <code>ProductData</code> class represents a generic data buffer used to hold the actual data values
stored in remote sensing data products.

 A single <code>ProductData</code> instance can have one or more elements of a primitive type. The primitive types
are: <ld> <li> {@link ProductData.Byte signed 8-bit integer} </li> <li> {@link ProductData.UByte unsigned 16-bit
integer} </li> <li> {@link ProductData.Short signed 32-bit integer} </li> <li> {@link ProductData.UShort unsigned
16-bit integer} </li> <li> {@link ProductData.Int signed 32-bit integer} </li> <li> {@link ProductData.UInt unsigned
32-bit integer} </li> <li> {@link ProductData.Float 32-bit floating point} </li> <li> {@link ProductData.Double
64-bit floating point} </li> <li> {@link ProductData.ASCII a character string (8-bit ASCII encoding)} </li> <li>
{@link ProductData.UTC a MJD-2000 encoded data/time value} </li> </ld>

The number of elements is an inmutable property of a <code>ProductData</code> instance.

In order to access the data in a <code>ProductData</code> instance, multiple setters and getters are provided
which use generic <i>transfer data types</i> in order to make the data transfer in and out of a
<code>ProductData</code> instance easy for programmers.<br> For scalar (one-element) values the prototypes are
<pre>
void setElem<b>Type</b>(<b>Type</b> elem);
<b>Type</b> getElem<b>Type</b>();
</pre>
For vector (multiple-element) values the prototypes are
<pre>
void setElem<b>Type</b>At(int index, <b>Type</b> elem);
<b>Type</b> getElem<b>Type</b>At(int index);
</pre>
Where the transfer data type <code><b>Type</b></code> is one of <code>int</code>, <code>long</code>,
<code>float</code>, <code>double</code> and <code>String</code>.
"""
class ProductData:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def createInstance(type):
        """
           Factory method which creates a value instance of the given type and with exactly one element.
           @param type the value's type
           @return a new value instance, <code>null</code> if the given type is not known
        """
        return ProductData(ProductData_createInstance1(type))

    @staticmethod
    def createInstance(type, numElems):
        """
           Factory method which creates a value instance of the given type and with the specified number of elements.
           @param type     the value's type
           @param numElems the number of elements, must be greater than zero if type is not {@link ProductData#TYPE_UTC}
           @return a new value instance, <code>null</code> if the given type is not known
           @throws IllegalArgumentException if one of the arguments is invalid
        """
        return ProductData(ProductData_createInstance2(type, numElems))

    @staticmethod
    def createInstance(type, data):
        """
           Factory method which creates a value instance of the given type and with the specified number of elements.
           @param type the value's type
           @param data if <code>type</code> is <code>TYPE_ASCII</code> the <code>String</code>, otherwise the primitive array type corresponding to <code>type</code>
           @return a new value instance, <code>null</code> if the given type is not known
           @throws IllegalArgumentException if one of the arguments is invalid
        """
        return ProductData(ProductData_createInstance3(type, data._obj))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance5(elems))

    @staticmethod
    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance1(elems))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance10(elems))

    @staticmethod
    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance3(elems))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance8(elems))

    @staticmethod
    def createUnsignedInstance(elems):
        return ProductData(ProductData_createUnsignedInstance2(elems))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance9(elems))

    @staticmethod
    def createInstance(strData):
        return ProductData(ProductData_createInstance4(strData))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance7(elems))

    @staticmethod
    def createInstance(elems):
        return ProductData(ProductData_createInstance6(elems))

    def getType(self):
        """
           Returns this value's type ID.
        """
        return ProductData_getType1(self._obj)

    @staticmethod
    def getElemSize(type):
        """
           Gets the element size of an element of the given type in bytes.
           @param type the element type
           @return the size of a single element in bytes.
           @throws IllegalArgumentException if the type is not supported.
        """
        return ProductData_getElemSize2(type)

    def getElemSize(self):
        """
           Gets the element size of an element of this product data in bytes.
           @return the size of a single element in bytes
        """
        return ProductData_getElemSize1(self._obj)

    @staticmethod
    def getTypeString(type):
        """
           Returns a textual representation of the given data type.
           @return a data type string, <code>null</code> if the type is unknown
        """
        return ProductData_getTypeString2(type)

    @staticmethod
    def getType(type):
        """
           Returns a integer representation of the given data type string.
           @return a data type integer, <code>null</code> if the type is unknown
        """
        return ProductData_getType2(type)

    def getTypeString(self):
        """
           Returns this value's data type String.
        """
        return ProductData_getTypeString1(self._obj)

    def isInt(self):
        """
           Tests whether this value has an integer.
           @return true, if so
        """
        return ProductData_isInt(self._obj)

    @staticmethod
    def isIntType(type):
        """
           Tests whether the given value type is a signed or unsigned integer type.
           @return true, if so
        """
        return ProductData_isIntType(type)

    def isSigned(self):
        """
           Tests whether the actual instance is an signed data type.
           @return true, if so
        """
        return ProductData_isSigned(self._obj)

    def isUnsigned(self):
        """
           Tests whether the actual instance is an unsigned data type.
           @return true, if so
        """
        return ProductData_isUnsigned(self._obj)

    @staticmethod
    def isUIntType(type):
        """
           Tests whether the given value type is an unsigned integer type.
           @return true, if so
        """
        return ProductData_isUIntType(type)

    @staticmethod
    def isFloatingPointType(type):
        """
           Tests whether the given value type is a floating point type.
           @return true, if so
        """
        return ProductData_isFloatingPointType(type)

    def isScalar(self):
        """
           Tests if this value is a scalar.
           @return true, if so
        """
        return ProductData_isScalar(self._obj)

    def getNumElems(self):
        """
           Returns the number of data elements this value has.
        """
        return ProductData_getNumElems(self._obj)

    def getElemInt(self):
        """
           Returns the value as an <code>int</code>. The method assumes that this value is a scalar and therefore simply
           returns <code>getElemIntAt(0)</code>.
           @see #getElemIntAt(int index)
        """
        return ProductData_getElemInt(self._obj)

    def getElemUInt(self):
        """
           Returns the value as an unsigned <code>int</code> given as a <code>long</code>. The method assumes that this
           value is a scalar and therefore simply returns <code>getElemUIntAt(0)</code>.
           @see #getElemUIntAt(int index)
        """
        return ProductData_getElemUInt(self._obj)

    def getElemFloat(self):
        """
           Returns the value as an <code>float</code>. The method assumes that this value is a scalar and therefore
           simply returns <code>getElemFloatAt(0)</code>.
           @see #getElemFloatAt(int index)
        """
        return ProductData_getElemFloat(self._obj)

    def getElemDouble(self):
        """
           Returns the value as an <code>double</code>. The method assumes that this value is a scalar and therefore
           simply returns <code>getElemDoubleAt(0)</code>.
           @see #getElemDoubleAt(int index)
        """
        return ProductData_getElemDouble(self._obj)

    def getElemString(self):
        """
           Returns the value as a <code>String</code>. The text returned is the comma-separated list of elements contained
           in this value.
           @return a text representing this fields value, never <code>null</code>
        """
        return ProductData_getElemString(self._obj)

    def getElemBoolean(self):
        """
           Returns the value as an <code>boolean</code>. The method assumes that this value is a scalar and therefore
           simply returns <code>getElemBooleanAt(0)</code>.
           @see #getElemBooleanAt(int index)
        """
        return ProductData_getElemBoolean(self._obj)

    def getElemIntAt(self, index):
        """
           Gets the value element with the given index as an <code>int</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemIntAt(self._obj, index)

    def getElemUIntAt(self, index):
        """
           Gets the value element with the given index as a <code>long</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemUIntAt(self._obj, index)

    def getElemFloatAt(self, index):
        """
           Gets the value element with the given index as a <code>float</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemFloatAt(self._obj, index)

    def getElemDoubleAt(self, index):
        """
           Gets the value element with the given index as a <code>double</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemDoubleAt(self._obj, index)

    def getElemStringAt(self, index):
        """
           Gets the value element with the given index as a <code>String</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemStringAt(self._obj, index)

    def getElemBooleanAt(self, index):
        """
           Gets the value element with the given index as a <code>boolean</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return ProductData_getElemBooleanAt(self._obj, index)

    def setElemInt(self, value):
        """
           Sets the value as an <code>int</code>. The method assumes that this value is a scalar and therefore simply
           calls <code>setElemInt(0, value)</code>.
           @param value the value to be set
           @see #setElemIntAt(int index, int value)
        """
        ProductData_setElemInt(self._obj, value)
        return

    def setElemUInt(self, value):
        """
           Sets the value as an unsigned <code>int</code> given as a <code>long</code>. The method assumes that this
           value is a scalar and therefore simply calls <code>setElemUInt(0, value)</code>.
           @param value the value to be set
           @see #setElemUIntAt(int index, long value)
        """
        ProductData_setElemUInt(self._obj, value)
        return

    def setElemFloat(self, value):
        """
           Sets the value as a <code>float</code>. The method assumes that this value is a scalar and therefore simply
           calls <code>setElemFloatAt(0, value)</code>.
           @param value the value to be set
           @see #setElemFloatAt(int index, float value)
        """
        ProductData_setElemFloat(self._obj, value)
        return

    def setElemDouble(self, value):
        """
           Sets the value as a <code>double</code>. The method assumes that this value is a scalar and therefore simply
           calls <code>setElemDoubleAt(0)</code>.
           @param value the value to be set
           @see #setElemDoubleAt(int index, double value)
        """
        ProductData_setElemDouble(self._obj, value)
        return

    def setElemString(self, value):
        """
           Sets the value as a <code>String</code>. The method assumes that this value is a scalar and therefore simply
           calls <code>setElemStringAt(0)</code>.
           @param value the value to be set
           @see #setElemStringAt
        """
        ProductData_setElemString(self._obj, value)
        return

    def setElemBoolean(self, value):
        """
           Sets the value as a <code>boolean</code>. The method assumes that this value is a scalar and therefore simply
           calls <code>setElemDoubleAt(0)</code>.
           @param value the value to be set
           @see #setElemBooleanAt(int index, boolean value)
        """
        ProductData_setElemBoolean(self._obj, value)
        return

    def setElemIntAt(self, index, value):
        """
           Sets the value at the specified index as an <code>int</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemIntAt(self._obj, index, value)
        return

    def setElemUIntAt(self, index, value):
        """
           Sets the value at the specified index as an unsigned <code>int</code> given as a <code>long</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemUIntAt(self._obj, index, value)
        return

    def setElemFloatAt(self, index, value):
        """
           Sets the value at the specified index as a <code>float</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemFloatAt(self._obj, index, value)
        return

    def setElemDoubleAt(self, index, value):
        """
           Sets the value at the specified index as a <code>double</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemDoubleAt(self._obj, index, value)
        return

    def setElemStringAt(self, index, value):
        """
           Sets the value at the specified index as a <code>String</code>.
           
           <i>THE METHOD IS CURRENTLY NOT IMPLEMENTED.</i>
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemStringAt(self._obj, index, value)
        return

    def setElemBooleanAt(self, index, value):
        """
           Sets the value at the specified index as a <code>boolean</code>.
           @param index the value index, must be <code>&gt;=0</code> and <code>&lt;getNumDataElems()</code>
           @param value the value to be set
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        ProductData_setElemBooleanAt(self._obj, index, value)
        return

    def getElems(self):
        """
           Returns the internal value. The actual type of the returned object should only be one of <ol>
           <li><code>byte[]</code> - for signed/unsigned 8-bit integer fields</li> <li><code>short[]</code> - for
           signed/unsigned 16-bit integer fields</li> <li><code>int[]</code> - for signed/unsigned 32-bit integer
           fields</li> <li><code>float[]</code> - for signed 32-bit floating point fields</li> <li><code>double[]</code> -
           for signed 64-bit floating point fields</li> </ol>
           @return an array of one of the described types
        """
        return Object(ProductData_getElems(self._obj))

    def setElems(self, data):
        """
           Sets the internal value. The actual type of the given data object should only be one of <ol>
           <li><code>byte[]</code> - for signed/unsigned 8-bit integer fields</li> <li><code>short[]</code> - for
           signed/unsigned 16-bit integer fields</li> <li><code>int[]</code> - for signed/unsigned 32-bit integer
           fields</li> <li><code>float[]</code> - for signed 32-bit floating point fields</li> <li><code>double[]</code> -
           for signed 64-bit floating point fields</li> <li><code>String[]</code> - for all field types</li> </ol>
           @param data an array of one of the described types
        """
        ProductData_setElems(self._obj, data._obj)
        return

    def readFrom(self, input):
        """
           Reads all elements of this <code>ProductData</code> instance from to the given input stream.
           
            The method subsequentially reads the elements at <code>0</code> to <code>getNumElems()-1</code> of this
           <code>ProductData</code> instance from the given input stream.<br> Reading starts at the current seek position
           within the input stream.
           @param input a seekable data input stream
           @throws IOException if an I/O error occurs
        """
        ProductData_readFrom4(self._obj, input._obj)
        return

    def readFrom(self, pos, input):
        """
           Reads a single element of this <code>ProductData</code> instance from to the given output stream.
           
            The method reads the element at <code>pos</code> of this <code>ProductData</code> instance from the given
           output stream.<br> Reading starts at the current seek position within the output stream.
           @param pos   the destination position (zero-based)
           @param input a seekable data input stream
           @throws IOException if an I/O error occurs
        """
        ProductData_readFrom3(self._obj, pos, input._obj)
        return

    def readFrom(self, startPos, numElems, input):
        """
           Reads elements of this <code>ProductData</code> instance from the given output stream.
           
            The method subsequentially reads the elements at <code>startPos</code> to <code>startPos+numElems-1</code> of
           this <code>ProductData</code> instance from the given input stream.<br> Reading starts at the current seek
           position of the input stream.
           @param startPos the destination start position (zero-based)
           @param numElems the number of elements to read
           @param input    a seekable data input stream
           @throws IOException if an I/O error occurs
        """
        ProductData_readFrom1(self._obj, startPos, numElems, input._obj)
        return

    def readFrom(self, startPos, numElems, input, inputPos):
        """
           Reads elements into this <code>ProductData</code> instance from the given input stream.
           
            The method subsequentially reads the elements at <code>startPos</code> to <code>startPos+numElems-1</code> of
           this <code>ProductData</code> instance from the given input stream.<br> Reading starts at <code>inputPos</code>
           within the output stream. The method multiplies this position with the value returned by
           <code>getElemSize()</code> in order to find the correct stream offset in bytes.
           @param startPos the destination start position (zero-based)
           @param numElems the number of elements to read
           @param input    a seekable data input stream
           @param inputPos the (zero-based) position in the data output stream where reading starts
           @throws IOException if an I/O error occurs
        """
        ProductData_readFrom2(self._obj, startPos, numElems, input._obj, inputPos)
        return

    def writeTo(self, output):
        """
           Writes all elements of this <code>ProductData</code> instance to to the given output stream.
           
            The method subsequentially writes the elements at <code>0</code> to <code>getNumElems()-1</code> of this
           <code>ProductData</code> instance to the given output stream.<br> Writing starts at the current seek position
           within the output stream.
           @param output a seekable data output stream
           @throws IOException if an I/O error occurs
        """
        ProductData_writeTo4(self._obj, output._obj)
        return

    def writeTo(self, pos, output):
        """
           Writes a single element of this <code>ProductData</code> instance to to the given output stream.
           
            The method writes the element at <code>pos</code> of this <code>ProductData</code> instance to the given
           output stream.<br> Writing starts at the current seek position within the output stream.
           @param pos    the source position (zero-based)
           @param output a seekable data output stream
           @throws IOException if an I/O error occurs
        """
        ProductData_writeTo3(self._obj, pos, output._obj)
        return

    def writeTo(self, startPos, numElems, output):
        """
           Writes elements of this <code>ProductData</code> instance to to the given output stream.
           
            The method subsequentially writes the elements at <code>startPos</code> to <code>startPos+numElems-1</code>
           of this <code>ProductData</code> instance to the given output stream.<br> Writing starts at the current seek
           position within the output stream.
           @param startPos the source start position (zero-based)
           @param numElems the number of elements to be written
           @param output   a seekable data output stream
           @throws IOException if an I/O error occurs
        """
        ProductData_writeTo1(self._obj, startPos, numElems, output._obj)
        return

    def writeTo(self, startPos, numElems, output, outputPos):
        """
           Writes elements of this <code>ProductData</code> instance to to the given output stream.
           
            The method subsequentially writes the elements at <code>startPos</code> to <code>startPos+numElems-1</code>
           of this <code>ProductData</code> instance to the given output stream.<br> Writing starts at
           <code>outputPos</code> within the output stream. The method multiplies this position with the value returned by
           <code>getElemSize()</code> in order to find the correct stream offset in bytes.
           @param startPos  the source start position (zero-based)
           @param numElems  the number of elements to be written
           @param output    a seekable data output stream
           @param outputPos the position in the data output stream where writing starts
           @throws IOException if an I/O error occurs
        """
        ProductData_writeTo2(self._obj, startPos, numElems, output._obj, outputPos)
        return

    def toString(self):
        """
           Returns a string representation of this value which can be used for debugging purposes.
        """
        return ProductData_toString(self._obj)

    def hashCode(self):
        """
           Returns {@link Object#hashCode()}.
        """
        return ProductData_hashCode(self._obj)

    def equals(self, other):
        """
           Returns {@link Object#equals(Object)}.
           Use {@link #equalElems} in order to perform an element-wise comparision.
        """
        return ProductData_equals(self._obj, other._obj)

    def equalElems(self, other):
        """
           Tests whether this ProductData is equal to another one.
           Performs an element-wise comparision if the other object is a {@link ProductData} instance of the same data type.
           Otherwise the method behaves like {@link Object#equals(Object)}.
           @param other the other one
        """
        return ProductData_equalElems(self._obj, other._obj)

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
        """
        ProductData_dispose(self._obj)
        return


class AffineTransform:
    def __init__(self, obj):
        self._obj = obj


"""
A {@code Mask} is used to mask image pixels of other raster data nodes.

This is a preliminary API under construction for BEAM 4.7. Not intended for public use.
"""
class Mask:
    def __init__(self, obj):
        self._obj = obj


class Double:
    def __init__(self, obj):
        self._obj = obj


"""
Provides the information required to decode integer sample values that
represent index values (e.g. types, classes, categories).
"""
class IndexCoding:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newIndexCoding(name):
        """
           Constructs a new index coding object with the given name.
           @param name the name
        """
        return IndexCoding(IndexCoding_newIndexCoding(name))

    def getIndex(self, name):
        """
           Returns a metadata attribute wich is the representation of the index with the given name. This method delegates to
           getPropertyValue(String).
           @param name the flag name
           @return a metadata attribute wich is the representation of the flag with the given name
        """
        return MetadataAttribute(IndexCoding_getIndex(self._obj, name))

    def getIndexNames(self):
        """
           Returns a string array which contains the names of all indexes contained in this <code>IndexCoding</code> object.
           @return a string array which contains all names of this <code>FlagCoding</code>.<br> If this
           <code>FlagCoding</code> does not contain any flag, <code>null</code> is returned
        """
        return IndexCoding_getIndexNames(self._obj)

    def addIndex(self, name, value, description):
        """
           Adds a new index definition to this flags coding.
           @param name        the index name
           @param value       the index value
           @param description the description text
           @throws IllegalArgumentException if <code>name</code> is null
           @return A new attribute representing the coded index.
        """
        return MetadataAttribute(IndexCoding_addIndex(self._obj, name, value, description))

    def getIndexValue(self, name):
        """
           Returns the flag mask value for the specified flag name.
           @param name the flag name
           @return flagMask the flag's bit mask as a 32 bit integer
           @throws IllegalArgumentException if <code>name</code> is null, or a flag with the name does not exist
        """
        return IndexCoding_getIndexValue(self._obj, name)

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method simply calls <code>visitor.visit(this)</code>.
           @param visitor the visitor, must not be <code>null</code>
        """
        IndexCoding_acceptVisitor(self._obj, visitor._obj)
        return

    def addElement(self, element):
        """
           Overrides the base class <code>addElement</code> in order to <b>not</b> add an element to this flag coding
           because flag codings do not support inner elements.
           @param element the element to be added, always ignored
        """
        IndexCoding_addElement(self._obj, element._obj)
        return

    def addAttribute(self, attribute):
        """
           Adds an attribute to this node. If an attribute with the same name already exists, the method does nothing.
           @param attribute the attribute to be added
           @throws IllegalArgumentException if the attribute added is not an integer or does not have a scalar value
        """
        IndexCoding_addAttribute(self._obj, attribute._obj)
        return

    def addSample(self, name, value, description):
        """
           Adds a new coding value to this sample coding.
           @param name        the coding name
           @param value       the value
           @param description the description text
           @throws IllegalArgumentException if <code>name</code> is null
           @return A new attribute representing the coded sample.
        """
        return MetadataAttribute(IndexCoding_addSample(self._obj, name, value, description))

    def getSampleCount(self):
        """
           Gets the number of coded sample values.
           @return the number of coded sample values
        """
        return IndexCoding_getSampleCount(self._obj)

    def getSampleName(self, index):
        """
           Gets the sample name at the specified attribute index.
           @param index the attribute index.
           @return the sample name.
        """
        return IndexCoding_getSampleName(self._obj, index)

    def getSampleValue(self, index):
        """
           Gets the sample value at the specified attribute index.
           @param index the attribute index.
           @return the sample value.
        """
        return IndexCoding_getSampleValue(self._obj, index)

    def getElementGroup(self):
        """
           Gets the group of child elements. The method returns null, if this element has no children.
           @return The child element group, may be null.
        """
        return ProductNodeGroup(IndexCoding_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(IndexCoding_getParentElement(self._obj))

    def addElementAt(self, element, index):
        """
           Adds the given element to this element at index.
           @param element the element to added, ignored if <code>null</code>
           @param index   where to put it
        """
        IndexCoding_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        """
           Removes the given element from this element.
           @param element the element to be removed, ignored if <code>null</code>
           @return true, if so
        """
        return IndexCoding_removeElement(self._obj, element._obj)

    def getNumElements(self):
        """
           @return the number of elements contained in this element.
        """
        return IndexCoding_getNumElements(self._obj)

    def getElementAt(self, index):
        """
           Returns the element at the given index.
           @param index the element index
           @return the element at the given index
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return MetadataElement(IndexCoding_getElementAt(self._obj, index))

    def getElementNames(self):
        """
           Returns a string array containing the names of the groups contained in this element
           @return a string array containing the names of the groups contained in this element. If this element has no
           groups a zero-length-array is returned.
        """
        return IndexCoding_getElementNames(self._obj)

    def getElements(self):
        """
           Returns an array of elements contained in this element.
           @return an array of elements contained in this product. If this element has no elements a zero-length-array is
           returned.
        """
        return MetadataElement(IndexCoding_getElements(self._obj))

    def getElement(self, name):
        """
           Returns the element with the given name.
           @param name the element name
           @return the element with the given name or <code>null</code> if a element with the given name is not contained in
           this element.
        """
        return MetadataElement(IndexCoding_getElement(self._obj, name))

    def containsElement(self, name):
        """
           Tests if a element with the given name is contained in this element.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a element with the given name is contained in this element, <code>false</code>
           otherwise
        """
        return IndexCoding_containsElement(self._obj, name)

    def getElementIndex(self, element):
        """
           Gets the index of the given element.
           @param element  The element .
           @return The element's index, or -1.
        """
        return IndexCoding_getElementIndex(self._obj, element._obj)

    def removeAttribute(self, attribute):
        """
           Removes the given attribute from this annotation. If an attribute with the same name already exists, the method
           does nothing.
           @param attribute the attribute to be removed, <code>null</code> is ignored
           @return <code>true</code> if it was removed
        """
        return IndexCoding_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        """
           Returns the number of attributes attaached to this node.
           @return the number of attributes
        """
        return IndexCoding_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        """
           Returns the attribute at the given index.
           @param index the attribute index
           @return the attribute, or <code>null</code> if this node does not contain attributes
           @throws IndexOutOfBoundsException
        """
        return MetadataAttribute(IndexCoding_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        """
           Returns the names of all attributes of this node.
           @return the attribute name array, never <code>null</code>
        """
        return IndexCoding_getAttributeNames(self._obj)

    def getAttributes(self):
        """
           Returns an array of attributes contained in this element.
           @return an array of attributes contained in this product. If this element has no attributes a zero-length-array
           is returned.
        """
        return MetadataAttribute(IndexCoding_getAttributes(self._obj))

    def getAttribute(self, name):
        """
           Returns the attribute with the given name.
           @param name the attribute name
           @return the attribute with the given name or <code>null</code> if it could not be found
        """
        return MetadataAttribute(IndexCoding_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        """
           Checks whether this node has an element with the given name.
           @param name the attribute name
           @return <code>true</code> if so
        """
        return IndexCoding_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        """
           Gets the index of the given attribute.
           @param attribute  The attribute.
           @return The attribute's index, or -1.
        """
        return IndexCoding_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        """
           Returns the double value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as double.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return IndexCoding_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        """
           Returns the UTC value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as UTC.
        """
        return ProductData_UTC(IndexCoding_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        """
           Returns the integer value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return IndexCoding_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        """
           Sets the attribute with the given name to the given integer value. A new attribute with
           <code>ProductData.TYPE_INT32</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        IndexCoding_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        """
           Sets the attribute with the given name to the given double value. A new attribute with
           <code>ProductData.TYPE_FLOAT64</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        IndexCoding_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        """
           Sets the attribute with the given name to the given utc value. A new attribute with
           <code>ProductData.UTC</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        IndexCoding_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        """
           Returns the string value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
        """
        return IndexCoding_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        """
           Sets the attribute with the given name to the given string value. A new attribute with
           <code>ProductData.TYPE_ASCII</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        IndexCoding_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        IndexCoding_setModified(self._obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(IndexCoding_createDeepClone(self._obj))

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        IndexCoding_dispose(self._obj)
        return

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(IndexCoding_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return IndexCoding_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        IndexCoding_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return IndexCoding_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        IndexCoding_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return IndexCoding_isModified(self._obj)

    def toString(self):
        return IndexCoding_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return IndexCoding_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(IndexCoding_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(IndexCoding_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(IndexCoding_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return IndexCoding_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return IndexCoding_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        IndexCoding_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        IndexCoding_removeFromFile(self._obj, productWriter._obj)
        return


class Term:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>RasterDataNode</code> class ist the abstract base class for all objects in the product package that contain
rasterized data. i.e. <code>Band</code> and <code>TiePointGrid</code>. It unifies the access to raster data in the
product model. A raster is considered as a rectangular raw data array with a fixed width and height. A raster data
node can scale its raw raster data samples in order to return geophysically meaningful pixel values.
@see #getRasterData()
@see #getRasterWidth()
@see #getRasterHeight()
@see #isScalingApplied()
@see #isLog10Scaled()
@see #getScalingFactor()
@see #getScalingOffset()
"""
class RasterDataNode:
    def __init__(self, obj):
        self._obj = obj


"""
A <code>PixelPos</code> represents a position or point in a pixel coordinate system.
"""
class PixelPos:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newPixelPos():
        """
           Constructs and initializes a <code>PixelPos</code> with coordinate (0,&nbsp;0).
        """
        return PixelPos(PixelPos_newPixelPos1())

    @staticmethod
    def newPixelPos(x, y):
        """
           Constructs and initializes a <code>PixelPos</code> with the specified coordinate.
           @param x the x component of the coordinate
           @param y the y component of the coordinate
        """
        return PixelPos(PixelPos_newPixelPos2(x, y))

    def isValid(self):
        """
           Tests whether or not this pixel position is valid.
           @return true, if so
        """
        return PixelPos_isValid(self._obj)

    def setInvalid(self):
        """
           Sets this pixel position so that is becomes invalid.
        """
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

    @staticmethod
    def distanceSq(arg0, arg1, arg2, arg3):
        return PixelPos_distanceSq2(arg0, arg1, arg2, arg3)

    @staticmethod
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


"""
AutoGrouping can be used by an application to auto-group a long list of product nodes (e.g. bands)
as a tree of product nodes.
"""
class Product_AutoGrouping:
    def __init__(self, obj):
        self._obj = obj


class ImageOutputStream:
    def __init__(self, obj):
        self._obj = obj


"""
Provides statistic information for a raster data node at a given image resolution level.
Instances of the <code>Stx</code> class are created using the {@link StxFactory}.

<i>Important note: This class has been revised in BEAM 4.10. All behaviour has been moved to {@link StxFactory}
leaving behind this class as a pure data container. Statistics are now furthermore derived upon
geo-physically interpreted image data (before it operated on the raw, unscaled data). Thus, it is
not required to scale the returned statistical properties, e.g. we used to write
{@code band.scale(stx.getMean())}. This is not required anymore.</i>
"""
class Stx:
    def __init__(self, obj):
        self._obj = obj


class Rectangle:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>ProductIO</code> class provides several utility methods concerning data I/O for remote sensing data
products.

 For example, a product can be read in using a single method call:
<pre>
Product product =  ProductIO.readProduct("test.prd");
</pre>
and written out in a similar way:
<pre>
ProductIO.writeProduct(product, "HDF5", "test.h5", null);
</pre>
"""
class ProductIO:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def getProductReader(formatName):
        """
           Gets a product reader for the given format name.
           @param formatName the product format name
           @return a suitable product reader or <code>null</code> if none was found
        """
        return ProductReader(ProductIO_getProductReader(formatName))

    @staticmethod
    def getProductWriterExtensions(formatName):
        """
           Gets an array of writer product file extensions for the given format name.
           @param formatName the format name
           @return an array of extensions or null if the format does not exist
        """
        return ProductIO_getProductWriterExtensions(formatName)

    @staticmethod
    def getProductWriter(formatName):
        """
           Gets a product writer for the given format name.
           @param formatName the product format name
           @return a suitable product writer or <code>null</code> if none was found
        """
        return ProductWriter(ProductIO_getProductWriter(formatName))

    @staticmethod
    def readProduct(filePath):
        """
           Reads the data product specified by the given file path.
           The product returned will be associated with the reader appropriate for the given
           file format (see also {@link Product#getProductReader() Product.productReader}).
           The method does not automatically read band data, thus
           {@link Band#getRasterData() Band.rasterData} will always be null
           for all bands in the product returned by this method.
           @param filePath the data product file path
           @return a data model as an in-memory representation of the given product file or <code>null</code> if no
           appropriate reader was found for the given product file
           @throws IOException if an I/O error occurs
           @see #readProduct(File)
        """
        return Product(ProductIO_readProduct(filePath))

    @staticmethod
    def getProductReaderForInput(input):
        """
           Tries to find a product reader instance suitable for the given input.
           The method returns {@code null}, if no
           registered product reader can handle the given {@code input} value.
           
           The {@code input} may be of any type, but most likely it will be a file path given by a {@code String} or
           {@code File} value. Some readers may also directly support an {@link javax.imageio.stream.ImageInputStream} object.
           @param input the input object.
           @return a product reader for the given {@code input} or {@code null} if no registered reader can handle
           the it.
           @see ProductReaderPlugIn#getDecodeQualification(Object)
           @see ProductReader#readProductNodes(Object, ProductSubsetDef)
        """
        return ProductReader(ProductIO_getProductReaderForInput(input._obj))

    @staticmethod
    def writeProduct(product, filePath, formatName):
        """
           Writes a product with the specified format to the given file path.
           The method also writes all band data to the file. Therefore the band data must either
           <ld>
           <li>be completely loaded ({@link Band#getRasterData() Band.rasterData} is not null)</li>
           <li>or the product must be associated with a product reader ({@link Product#getProductReader() Product.productReader} is not null) so that unloaded data can be reloaded.</li>
           </ld>.
           @param product    the product, must not be <code>null</code>
           @param filePath   the file path
           @param formatName the name of a supported product format, e.g. "HDF5". If <code>null</code>, the default format
           "BEAM-DIMAP" will be used
           @throws IOException if an IOException occurs
        """
        ProductIO_writeProduct(product._obj, filePath, formatName)
        return


"""
The <code>ProductNode</code> is the base class for all nodes within a remote sensing data product and even the data
product itself.
"""
class ProductNode:
    def __init__(self, obj):
        self._obj = obj


class Dimension:
    def __init__(self, obj):
        self._obj = obj


class AngularDirection:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newAngularDirection(azimuth, zenith):
        return AngularDirection(AngularDirection_newAngularDirection(azimuth, zenith))

    def equals(self, obj):
        return AngularDirection_equals(self._obj, obj._obj)

    def toString(self):
        return AngularDirection_toString(self._obj)


class SimpleFeatureType:
    def __init__(self, obj):
        self._obj = obj


"""
Provides the information required to decode integer sample values that
are combined of single flags (bit indexes).
"""
class SampleCoding:
    def __init__(self, obj):
        self._obj = obj


class Object:
    def __init__(self, obj):
        self._obj = obj


"""
Classes implementing the <code>ProductReader</code> interface know how to create an in-memory representation of a
given data product as input source.
@see ProductWriter
"""
class ProductReader:
    def __init__(self, obj):
        self._obj = obj

    def getReaderPlugIn(self):
        """
           Returns the plug-in which created this product reader.
           @return the product reader plug-in, should never be <code>null</code>
        """
        return ProductReaderPlugIn(ProductReader_getReaderPlugIn(self._obj))

    def getInput(self):
        """
           Retrives the current input destination object. Thie return value might be <code>null</code> if the
           <code>setInput</code> has not been called so far.
        """
        return Object(ProductReader_getInput(self._obj))

    def getSubsetDef(self):
        """
           Returns the subset information with which this a data product is read from its physical source.
           @return the subset information, can be <code>null</code>
        """
        return ProductSubsetDef(ProductReader_getSubsetDef(self._obj))

    def readProductNodes(self, input, subsetDef):
        """
           Reads a data product and returns a in-memory representation of it.
           
            The given subset info can be used to specify spatial and spectral portions of the original proudct. If the
           subset is omitted, the complete product is read in.
           
            Whether the band data - the actual pixel values - is read in immediately or later when pixels are requested,
           is up to the implementation.
           @param input     an object representing a valid output for this product reader, might be a
           <code>ImageInputStream</code> or other <code>Object</code> to use for future decoding.
           @param subsetDef a spectral or spatial subset (or both) of the product. If <code>null</code>, the entire product
           is read in
           @throws IllegalArgumentException   if <code>input</code> is <code>null</code> or it's type is not one of the
           supported input sources.
           @throws IOException                if an I/O error occurs
           @throws IllegalFileFormatException if the file format is illegal
        """
        return Product(ProductReader_readProductNodes(self._obj, input._obj, subsetDef._obj))

    def readBandRasterData(self, destBand, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer, pm):
        """
           Reads raster data from the data source specified by the given destination band into the given in-memory buffer
           and region.
           
           <h3>Destination band</h3> The destination band is used to identify the data source from which this method
           transfers the sample values into the given destination buffer. The method does not modify the given destination
           band at all. If this product reader has a <code>ProductSubsetDef</code> instance attached to it, the method
           should also consider the specified spatial subset and sub-sampling (if any) applied to the destination band.
           
           <h3>Destination region</h3> The given destination region specified by the <code>destOffsetX</code>,
           <code>destOffsetY</code>, <code>destWidth</code> and <code>destHeight</code> parameters are given in the band's
           raster co-ordinates of the raster which results <i>after</i> applying the optional spatial subset and
           sub-sampling given by the <code>ProductSubsetDef</code> instance to the <i>data source</i>. If no spatial subset
           and sub-sampling is specified, the destination co-ordinates are identical with the source co-ordinates. The
           destination region should always specify a sub-region of the band's scene raster.
           
           <h3>Destination buffer</h3> The first element of the destination buffer corresponds to the given
           <code>destOffsetX</code> and <code>destOffsetY</code> of the destination region. The offset parameters are
           <b>not</b> an offset within the buffer.<br> The number of elements in the buffer exactly be <code>destWidth *
           destHeight</code>. The pixel values read are stored in line-by-line order, so the raster X co-ordinate varies
           faster than the Y co-ordinate.
           @param destBand    the destination band which identifies the data source from which to read the sample values
           @param destOffsetX the X-offset in the band's raster co-ordinates
           @param destOffsetY the Y-offset in the band's raster co-ordinates
           @param destWidth   the width of region to be read given in the band's raster co-ordinates
           @param destHeight  the height of region to be read given in the band's raster co-ordinates
           @param destBuffer  the destination buffer which receives the sample values to be read
           @throws IOException              if an I/O error occurs
           @throws IllegalArgumentException if the number of elements destination buffer not equals <code>destWidth *
           destHeight</code> or the destination region is out of the band's scene raster
           @see Band#getSceneRasterWidth()
           @see Band#getSceneRasterHeight()
        """
        ProductReader_readBandRasterData(self._obj, destBand._obj, destOffsetX, destOffsetY, destWidth, destHeight, destBuffer._obj, pm._obj)
        return

    def close(self):
        """
           Closes the access to all currently opened resources such as file input streams and all resources of this children
           directly owned by this reader. Its primary use is to allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>close()</code> are undefined.
           
           Overrides of this method should always call <code>super.close();</code> after disposing this instance.
           @throws IOException if an I/O error occurs
        """
        ProductReader_close(self._obj)
        return


"""
The <code>ProductReaderPlugIn</code> interface is implemented by data product reader plug-ins.

XMLDecoder plug-ins are used to provide meta-information about a particular data format and to create instances of
the actual reader objects.

 A plug-in can register itself in the <code>ProductIO</code> plug-in registry or it is automatically found during
a classpath scan.
@see ProductWriterPlugIn
"""
class ProductReaderPlugIn:
    def __init__(self, obj):
        self._obj = obj


class Integer:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>ProductData.UTC</code> class is a <code>ProductData.UInt</code> specialisation for UTC date/time
values.

 Internally, data is stored in an <code>int[3]</code> array which represents a Modified Julian Day 2000
({@link ProductData.UTC#getMJD() MJD}) as a {@link
ProductData.UTC#getDaysFraction() days}, a {@link
ProductData.UTC#getSecondsFraction() seconds} and a {@link
ProductData.UTC#getMicroSecondsFraction() micro-seconds} fraction.
@see ProductData.UTC#getMJD()
@see ProductData.UTC#getDaysFraction()
@see ProductData.UTC#getSecondsFraction()
@see ProductData.UTC#getMicroSecondsFraction()
"""
class ProductData_UTC:
    def __init__(self, obj):
        self._obj = obj


"""
A band contains the data for geophysical parameter in remote sensing data products. Bands are two-dimensional images
which hold their pixel values (samples) in a buffer of the type {@link ProductData}. The band class is just a
container for attached metadata of the band, currently: <ul> <li>the flag coding {@link FlagCoding}</li> <li>the band
index at which position the band is stored in the associated product</li> <li>the center wavelength of the band</li>
<li>the bandwidth of the band</li> <li>the solar spectral flux of the band</li> <li>the width and height of the
band</li> </ul> The band can contain a buffer to the real data, but this buffer must be read explicitely, to keep the
memory fingerprint small, the data is not read automatically.


The several <code>getPixel</code> and <code>readPixel</code> methods of this class do not necessarily return the
values contained in the data buffer of type {@link ProductData}. If the <code>scalingFactor</code>,
<code>scalingOffset</code> or <code>log10Scaled</code> are set a conversion of the form <code>scalingFactor *
rawSample + scalingOffset</code> is applied to the raw samples before the <code>getPixel</code> and @
<code>readPixel</code> methods return the actual pixel values. If the <code>log10Scaled</code> property is true then
the conversion is <code>pow(10, scalingFactor * rawSample + scalingOffset)</code>. The several <code>setPixel</code>
and <code>writePixel</code> perform the inverse operations in this case.
@see ProductData
"""
class Band:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newBand(name, dataType, width, height):
        """
           Constructs a new <code>Band</code>.
           @param name     the name of the new object
           @param dataType the raster data type, must be one of the multiple <code>ProductData.TYPE_<i>X</i></code>
           constants, with the exception of <code>ProductData.TYPE_UINT32</code>
           @param width    the width of the raster in pixels
           @param height   the height of the raster in pixels
        """
        return Band(Band_newBand(name, dataType, width, height))

    def getFlagCoding(self):
        """
           Gets the flag coding for this band.
           @return a non-null value if this band is a flag dataset, <code>null</code> otherwise
        """
        return FlagCoding(Band_getFlagCoding(self._obj))

    def isFlagBand(self):
        """
           Tests whether or not this band is a flag band (<code>getFlagCoding() != null</code>).
           @return <code>true</code> if so
        """
        return Band_isFlagBand(self._obj)

    def getIndexCoding(self):
        """
           Gets the index coding for this band.
           @return a non-null value if this band is a flag dataset, <code>null</code> otherwise
        """
        return IndexCoding(Band_getIndexCoding(self._obj))

    def isIndexBand(self):
        """
           Tests whether or not this band is an index band (<code>getIndexCoding() != null</code>).
           @return <code>true</code> if so
        """
        return Band_isIndexBand(self._obj)

    def getSampleCoding(self):
        """
           Gets the sample coding.
           @return the sample coding, or {@value null} if not set.
        """
        return SampleCoding(Band_getSampleCoding(self._obj))

    def setSampleCoding(self, sampleCoding):
        """
           Sets the sample coding for this band.
           @param sampleCoding the sample coding
           @throws IllegalArgumentException if this band does not contain integer pixels
        """
        Band_setSampleCoding(self._obj, sampleCoding._obj)
        return

    def getSpectralBandIndex(self):
        """
           Gets the (zero-based) spectral band index.
           @return the (zero-based) spectral band index or <code>-1</code> if it is unknown
        """
        return Band_getSpectralBandIndex(self._obj)

    def setSpectralBandIndex(self, spectralBandIndex):
        """
           Sets the (zero-based) spectral band index.
           @param spectralBandIndex the (zero-based) spectral band index or <code>-1</code> if it is unknown
        """
        Band_setSpectralBandIndex(self._obj, spectralBandIndex)
        return

    def getSpectralWavelength(self):
        """
           Gets the spectral wavelength in <code>nm</code> (nanomater) units.
           @return the wave length in nanometers of this band, or zero if this is not a spectral band or the wave length is
           not known.
        """
        return Band_getSpectralWavelength(self._obj)

    def setSpectralWavelength(self, spectralWavelength):
        """
           Sets the spectral wavelength in <code>nm</code> (nanomater) units.
           @param spectralWavelength the wavelength in nanometers of this band, or zero if this is not a spectral band or
           the wavelength is not known.
        """
        Band_setSpectralWavelength(self._obj, spectralWavelength)
        return

    def getSpectralBandwidth(self):
        """
           Gets the spectral bandwidth in <code>nm</code> (nanomater) units.
           @return the bandwidth in nanometers of this band, or zero if this is not a spectral band or the bandwidth is not
           known.
        """
        return Band_getSpectralBandwidth(self._obj)

    def setSpectralBandwidth(self, spectralBandwidth):
        """
           Sets the spectral bandwidth in <code>nm</code> (nanomater) units.
           @param spectralBandwidth the spectral bandwidth in nanometers of this band, or zero if this is not a spectral band
           or the spectral bandwidth is not known.
        """
        Band_setSpectralBandwidth(self._obj, spectralBandwidth)
        return

    def getSolarFlux(self):
        """
           Gets the solar flux in <code>mW/(m^2 nm)</code> (milli-watts per square metre per nanometer)
           units for the wavelength of this band.
           @return the solar flux for the wavelength of this band, or zero if this is not a spectral band or the solar flux
           is not known.
        """
        return Band_getSolarFlux(self._obj)

    def setSolarFlux(self, solarFlux):
        """
           Sets the solar flux in <code>mW/(m^2 nm)</code> (milli-watts per square metre per nanometer)
           units for the wavelength of this band.
           @param solarFlux the solar flux for the wavelength of this band, or zero if this is not a spectral band or the
           solar flux is not known.
        """
        Band_setSolarFlux(self._obj, solarFlux)
        return

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method simply calls <code>visitor.visit(this)</code>.
           @param visitor the visitor, must not be <code>null</code>
        """
        Band_acceptVisitor(self._obj, visitor._obj)
        return

    def toString(self):
        """
           Creates a string defining this band object.
        """
        return Band_toString(self._obj)

    def removeFromFile(self, productWriter):
        Band_removeFromFile(self._obj, productWriter._obj)
        return

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        Band_dispose(self._obj)
        return

    def getSceneRasterData(self):
        """
           Gets a raster data holding this band's pixel data for an entire product scene. If the data has'nt been loaded so
           far the method returns <code>null</code>.
           
           In opposite to the <code>getRasterData</code> method, this method returns raster data that has at least
           <code>getBandOutputRasterWidth()*getBandOutputRasterHeight()</code> elements of the given data type to store the
           scene's pixels.
           @return raster data covering the pixels for a complete scene
           @see #getRasterData
           @see RasterDataNode#getSceneRasterWidth
           @see RasterDataNode#getSceneRasterHeight
        """
        return ProductData(Band_getSceneRasterData(self._obj))

    def getPixelInt(self, x, y):
        """
           Gets the sample for the pixel located at (x,y) as an integer value.
           @param x The X co-ordinate of the pixel location
           @param y The Y co-ordinate of the pixel location
           @throws NullPointerException if this band has no raster data
           @throws java.lang.ArrayIndexOutOfBoundsException
           if the co-ordinates are not in bounds
        """
        return Band_getPixelInt(self._obj, x, y)

    def getPixelFloat(self, x, y):
        """
           Gets the sample for the pixel located at (x,y) as a float value.
           @param x The X co-ordinate of the pixel location
           @param y The Y co-ordinate of the pixel location
           @throws NullPointerException if this band has no raster data
           @throws java.lang.ArrayIndexOutOfBoundsException
           if the co-ordinates are not in bounds
        """
        return Band_getPixelFloat(self._obj, x, y)

    def getPixelDouble(self, x, y):
        """
           Gets the sample for the pixel located at (x,y) as a double value.
           @param x The X co-ordinate of the pixel location
           @param y The Y co-ordinate of the pixel location
           @throws NullPointerException if this band has no raster data
           @throws java.lang.ArrayIndexOutOfBoundsException
           if the co-ordinates are not in bounds
        """
        return Band_getPixelDouble(self._obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        """
           Sets the pixel at the given pixel co-ordinate to the given pixel value.
           @param x          The X co-ordinate of the pixel location
           @param y          The Y co-ordinate of the pixel location
           @param pixelValue the new pixel value
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelInt(self._obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        """
           Sets the pixel at the given pixel coordinate to the given pixel value.
           @param x          The X co-ordinate of the pixel location
           @param y          The Y co-ordinate of the pixel location
           @param pixelValue the new pixel value
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelFloat(self._obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        """
           Sets the pixel value at the given pixel coordinate to the given pixel value.
           @param x          The X co-ordinate of the pixel location
           @param y          The Y co-ordinate of the pixel location
           @param pixelValue the new pixel value
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelDouble(self._obj, x, y, pixelValue)
        return

    def setPixels(self, x, y, w, h, pixels):
        """
           Sets a range of pixels specified by the coordinates as integer array. Copies the data to the memory buffer of
           data at the specified location. Throws exception when the target buffer is not in memory.
           @param x      x offset into the band
           @param y      y offset into the band
           @param w      width of the pixel array to be written
           @param h      height of the pixel array to be written.
           @param pixels integer array to be written
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelsInt(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        """
           Sets a range of pixels specified by the coordinates as float array. Copies the data to the memory buffer of data
           at the specified location. Throws exception when the target buffer is not in memory.
           @param x      x offset into the band
           @param y      y offset into the band
           @param w      width of the pixel array to be written
           @param h      height of the pixel array to be written.
           @param pixels float array to be written
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelsFloat(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        """
           Sets a range of pixels specified by the coordinates as double array. Copies the data to the memory buffer of data
           at the specified location. Throws exception when the target buffer is not in memory.
           @param x      x offset into the band
           @param y      y offset into the band
           @param w      width of the pixel array to be written
           @param h      height of the pixel array to be written.
           @param pixels double array to be written
           @throws NullPointerException if this band has no raster data
        """
        Band_setPixelsDouble(self._obj, x, y, w, h, pixels)
        return

    def ensureRasterData(self):
        """
           Ensures that raster data exists
        """
        Band_ensureRasterData(self._obj)
        return

    def unloadRasterData(self):
        """
           Un-loads the raster data for this band.
           
           After this method has been called successfully, the <code>hasRasterData()</code> method returns
           <code>false</code> and <code>getRasterData()</code> returns <code>null</code>.
           
           @see #loadRasterData()
        """
        Band_unloadRasterData(self._obj)
        return

    def getViewModeId(self, bandName):
        return Band_getViewModeId(self._obj, bandName)

    def getSceneRasterWidth(self):
        """
           Returns the width in pixels of the scene represented by this product raster. By default, the method simply
           returns <code>getRasterWidth()</code>.
           @return the scene width in pixels
        """
        return Band_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        """
           Returns the height in pixels of the scene represented by this product raster. By default, the method simply
           returns <code>getRasterHeight()</code>.
           @return the scene height in pixels
        """
        return Band_getSceneRasterHeight(self._obj)

    def getRasterWidth(self):
        """
           Returns the width of the raster used by this product raster.
           @return the width of the raster
        """
        return Band_getRasterWidth(self._obj)

    def getRasterHeight(self):
        """
           Returns the height of the raster used by this product raster.
           @return the height of the raster
        """
        return Band_getRasterHeight(self._obj)

    def setModified(self, modified):
        Band_setModified(self._obj, modified)
        return

    def getGeoCoding(self):
        """
           Returns the geo-coding of this {@link RasterDataNode}.
           @return the geo-coding
        """
        return GeoCoding(Band_getGeoCoding(self._obj))

    def setGeoCoding(self, geoCoding):
        """
           Sets the geo-coding for this {@link RasterDataNode}.
           Also sets the geo-coding of the parent {@link Product} if it has no geo-coding yet.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_GEOCODING}.
           @param geoCoding the new geo-coding
           @see Product#setGeoCoding(GeoCoding)
        """
        Band_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getPointing(self):
        """
           Gets a {@link Pointing} if one is available for this raster.
           The methods calls {@link #createPointing()} if a pointing has not been set so far or if its {@link GeoCoding} changed
           since the last creation of this raster's {@link Pointing} instance.
           @return the pointing object, or null if a pointing is not available
        """
        return Pointing(Band_getPointing(self._obj))

    def canBeOrthorectified(self):
        """
           Tests if this raster data node can be orthorectified.
           @return true, if so
        """
        return Band_canBeOrthorectified(self._obj)

    def isFloatingPointType(self):
        """
           Returns <code>true</code> if the pixel data contained in this band is "naturally" a floating point number type.
           @return true, if so
        """
        return Band_isFloatingPointType(self._obj)

    def getGeophysicalDataType(self):
        """
           Returns the geophysical data type of this <code>RasterDataNode</code>. The value returned is always one of the
           <code>ProductData.TYPE_XXX</code> constants.
           @return the geophysical data type
           @see ProductData
           @see #isScalingApplied()
        """
        return Band_getGeophysicalDataType(self._obj)

    def getScalingFactor(self):
        """
           Gets the scaling factor which is applied to raw {@link <code>ProductData</code>}. The default value is
           <code>1.0</code> (no factor).
           @return the scaling factor
           @see #isScalingApplied()
        """
        return Band_getScalingFactor(self._obj)

    def setScalingFactor(self, scalingFactor):
        """
           Sets the scaling factor which is applied to raw {@link <code>ProductData</code>}.
           @param scalingFactor the scaling factor
           @see #isScalingApplied()
        """
        Band_setScalingFactor(self._obj, scalingFactor)
        return

    def getScalingOffset(self):
        """
           Gets the scaling offset which is applied to raw {@link <code>ProductData</code>}. The default value is
           <code>0.0</code> (no offset).
           @return the scaling offset
           @see #isScalingApplied()
        """
        return Band_getScalingOffset(self._obj)

    def setScalingOffset(self, scalingOffset):
        """
           Sets the scaling offset which is applied to raw {@link <code>ProductData</code>}.
           @param scalingOffset the scaling offset
           @see #isScalingApplied()
        """
        Band_setScalingOffset(self._obj, scalingOffset)
        return

    def isLog10Scaled(self):
        """
           Gets whether or not the {@link <code>ProductData</code>} of this band has a negative binominal distribution and
           thus the common logarithm (base 10) of the values is stored in the raw data. The default value is
           <code>false</code>.
           @return whether or not the data is logging-10 scaled
           @see #isScalingApplied()
        """
        return Band_isLog10Scaled(self._obj)

    def setLog10Scaled(self, log10Scaled):
        """
           Sets whether or not the {@link <code>ProductData</code>} of this band has a negative binominal distribution and
           thus the common logarithm (base 10) of the values is stored in the raw data.
           @param log10Scaled whether or not the data is logging-10 scaled
           @see #isScalingApplied()
        """
        Band_setLog10Scaled(self._obj, log10Scaled)
        return

    def isScalingApplied(self):
        """
           Tests whether scaling of raw raster data values is applied before they are returned as geophysically meaningful
           pixel values. The methods which return geophysical pixel values are all {@link #getPixels(int, int, int, int, int[])},
           {@link #setPixels(int, int, int, int, int[])}, {@link #readPixels(int, int, int, int, int[])} and
           {@link #writePixels(int, int, int, int, int[])} methods as well as the <code>getPixel&lt;Type&gt;</code> and
           <code>setPixel&lt;Type&gt;</code> methods such as  {@link #getPixelFloat(int, int)} * and
           {@link #setPixelFloat(int, int, float)}.
           @return <code>true</code> if a conversion is applyied to raw data samples before the are retuned.
           @see #getScalingOffset
           @see #getScalingFactor
           @see #isLog10Scaled
        """
        return Band_isScalingApplied(self._obj)

    @staticmethod
    def isValidMaskProperty(propertyName):
        """
           Tests if the given name is the name of a property which is relevant for the computation of the valid mask.
           @param propertyName the  name to test
           @return {@code true}, if so.
        """
        return Band_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        """
           Tests whether or not a no-data value has been specified. The no-data value is not-specified unless either
           {@link #setNoDataValue(double)} or {@link #setGeophysicalNoDataValue(double)} is called.
           @return true, if so
           @see #isNoDataValueUsed()
           @see #setNoDataValue(double)
        """
        return Band_isNoDataValueSet(self._obj)

    def clearNoDataValue(self):
        """
           Clears the no-data value, so that {@link #isNoDataValueSet()} will return <code>false</code>.
        """
        Band_clearNoDataValue(self._obj)
        return

    def isNoDataValueUsed(self):
        """
           Tests whether or not the no-data value is used.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return true, if so
           @see #setNoDataValueUsed(boolean)
           @see #isNoDataValueSet()
        """
        return Band_isNoDataValueUsed(self._obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        """
           Sets whether or not the no-data value is used.
           If the no-data value is enabled and the no-data value has not been set so far,
           a default no-data value it is set with a value of to zero.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE_USED}.
           @param noDataValueUsed true, if so
           @see #isNoDataValueUsed()
        """
        Band_setNoDataValueUsed(self._obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        """
           Gets the no-data value as a primitive <code>double</code>.
           Note that the value returned is NOT necessarily the same as the value returned by
           {@link #getGeophysicalNoDataValue()} because no scaling is applied.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           The method returns <code>0.0</code>, if no no-data value has been specified so far.
           @return the no-data value. It is returned as a <code>double</code> in order to cover all other numeric types.
           @see #setNoDataValue(double)
           @see #isNoDataValueSet()
        """
        return Band_getNoDataValue(self._obj)

    def setNoDataValue(self, noDataValue):
        """
           Sets the no-data value as a primitive <code>double</code>.
           Note that the given value is related to the "raw", un-scaled raster data.
           In order to set the geophysical, scaled no-data value use the method
           {@link #setGeophysicalNoDataValue(double)}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE}.
           @param noDataValue the no-data value. It is passed as a <code>double</code> in order to cover all other numeric types.
           @see #getNoDataValue()
           @see #isNoDataValueSet()
        """
        Band_setNoDataValue(self._obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        """
           Gets the geophysical no-data value which is simply the scaled "raw" no-data value
           returned by {@link #getNoDataValue()}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return the geophysical no-data value
           @see #setGeophysicalNoDataValue(double)
        """
        return Band_getGeophysicalNoDataValue(self._obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        """
           Sets the geophysical no-data value which is simply the scaled "raw" no-data value
           returned by {@link #getNoDataValue()}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE}.
           @param noDataValue the new geophysical no-data value
           @see #setGeophysicalNoDataValue(double)
           @see #isNoDataValueSet()
        """
        Band_setGeophysicalNoDataValue(self._obj, noDataValue)
        return

    def getValidPixelExpression(self):
        """
           Gets the expression that is used to determine whether a pixel is valid or not.
           For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return the valid mask expression.
        """
        return Band_getValidPixelExpression(self._obj)

    def setValidPixelExpression(self, validPixelExpression):
        """
           Sets the expression that is used to determine whether a pixel is valid or not.
           The valid-pixel expression is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_VALID_PIXEL_EXPRESSION}.
           @param validPixelExpression the valid mask expression, can be null
        """
        Band_setValidPixelExpression(self._obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        """
           Tests whether or not this raster data node uses a data-mask in order to determine valid pixels. The method returns
           true if either {@link #isValidPixelExpressionSet()} or {@link #isNoDataValueUsed()} returns true.
           The data-mask is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return true, if so
        """
        return Band_isValidMaskUsed(self._obj)

    def resetValidMask(self):
        """
           Resets the valid mask of this raster.
           The mask will be lazily regenerated when requested the next time.
        """
        Band_resetValidMask(self._obj)
        return

    def getValidMaskExpression(self):
        """
           Gets the expression used for the computation of the mask which identifies valid pixel values.
           It recognizes the value of the {@link #getNoDataValue() noDataValue} and the
           {@link #getValidPixelExpression() validPixelExpression} properties, if any.
           The method returns {@code null},  if none of these properties are set.
           @return The expression used for the computation of the mask which identifies valid pixel values,
           or {@code null}.
           @see #getValidPixelExpression()
           @see #getNoDataValue()
        """
        return Band_getValidMaskExpression(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           {@inheritDoc}
        """
        Band_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        """
           Returns true if the raster data of this <code>RasterDataNode</code> is loaded or elsewhere available, otherwise
           false.
           @return true, if so.
        """
        return Band_hasRasterData(self._obj)

    def getRasterData(self):
        """
           Gets the raster data for this dataset. If the data has'nt been loaded so far the method returns
           <code>null</code>.
           @return the raster data for this band, or <code>null</code> if data has not been loaded
        """
        return ProductData(Band_getRasterData(self._obj))

    def setRasterData(self, rasterData):
        """
           Sets the raster data of this dataset.
           
            Note that this method does not copy data at all. If the supplied raster data is compatible with this product
           raster, then simply its reference is stored. Modifications in the supplied raster data will also affect this
           dataset's data!
           @param rasterData the raster data for this dataset
           @see #getRasterData()
        """
        Band_setRasterData(self._obj, rasterData._obj)
        return

    def loadRasterData(self):
        """
           @throws java.io.IOException if an I/O error occurs
           @see #loadRasterData(com.bc.ceres.core.ProgressMonitor)
        """
        Band_loadRasterData(self._obj)
        return

    def isPixelValid(self, x, y):
        """
           Checks whether or not the pixel located at (x,y) is valid.
           A pixel is assumed to be valid either if  {@link #getValidMaskImage() validMaskImage} is null or
           or if the bit corresponding to (x,y) is set within the returned mask image.
           
           <i>Note: Implementation changed by Norman (2011-08-09) in order to increase performance since
           a synchronised block was used due to problem with the JAI ROI class that has been used in
           the former implementation.</i>
           @param x the X co-ordinate of the pixel location
           @param y the Y co-ordinate of the pixel location
           @return <code>true</code> if the pixel is valid
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
           @see #isPixelValid(int, int, javax.media.jai.ROI)
           @see #setNoDataValueUsed(boolean)
           @see #setNoDataValue(double)
           @see #setValidPixelExpression(String)
        """
        return Band_isPixelValid(self._obj, x, y)

    def getSampleInt(self, x, y):
        """
           Gets a geo-physical sample value at the given pixel coordinate as {@code int} value.
           
           <i>Note: This method does not belong to the public API.
           It has been added by Norman (2011-08-09) in order to perform performance tests.</i>
           @param x pixel X coordinate
           @param y pixel Y coordinate
           @return The geo-physical sample value.
        """
        return Band_getSampleInt(self._obj, x, y)

    def getSampleFloat(self, x, y):
        """
           Gets a geo-physical sample value at the given pixel coordinate as {@code float} value.
           
           <i>Note: This method does not belong to the public API.
           It has been added by Norman (2011-08-09) in order to perform performance tests.</i>
           @param x pixel X coordinate
           @param y pixel Y coordinate
           @return The geo-physical sample value.
        """
        return Band_getSampleFloat(self._obj, x, y)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, int[], ProgressMonitor)
        """
        return Band_getPixelsInt(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, float[], ProgressMonitor)
        """
        return Band_getPixelsFloat(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, double[], ProgressMonitor)
        """
        return Band_getPixelsDouble(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, int[], ProgressMonitor)
        """
        return Band_readPixelsInt(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, float[], ProgressMonitor)
        """
        return Band_readPixelsFloat(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, double[], ProgressMonitor)
        """
        return Band_readPixelsDouble(self._obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, int[], ProgressMonitor)
        """
        Band_writePixelsInt(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, float[], ProgressMonitor)
        """
        Band_writePixelsFloat(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, double[], ProgressMonitor)
        """
        Band_writePixelsDouble(self._obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return Band_readValidMask(self._obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        """
           @throws java.io.IOException if an I/O error occurs
           @see #readRasterDataFully(ProgressMonitor)
        """
        Band_readRasterDataFully(self._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        """
           Reads raster data from the node's associated data source into the given data
           buffer.
           @param offsetX    the X-offset in the raster co-ordinates where reading starts
           @param offsetY    the Y-offset in the raster co-ordinates where reading starts
           @param width      the width of the raster data buffer
           @param height     the height of the raster data buffer
           @param rasterData a raster data buffer receiving the pixels to be read
           @throws java.io.IOException      if an I/O error occurs
           @throws IllegalArgumentException if the raster is null
           @throws IllegalStateException    if this product raster was not added to a product so far, or if the product to
           which this product raster belongs to, has no associated product reader
           @see ProductReader#readBandRasterData(Band, int, int, int, int, ProductData, com.bc.ceres.core.ProgressMonitor)
        """
        Band_readRasterData(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def writeRasterDataFully(self):
        Band_writeRasterDataFully(self._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        Band_writeRasterData(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def createCompatibleRasterData(self):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>getRasterWidth()*getRasterHeight()</code> elements of a compatible data type.
           @return raster data compatible with this product raster
           @see #createCompatibleSceneRasterData
        """
        return ProductData(Band_createCompatibleRasterData(self._obj))

    def createCompatibleSceneRasterData(self):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>getBandOutputRasterWidth()*getBandOutputRasterHeight()</code> elements of a compatible data type.
           @return raster data compatible with this product raster
           @see #createCompatibleRasterData
        """
        return ProductData(Band_createCompatibleSceneRasterData(self._obj))

    def createCompatibleRasterData(self, width, height):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>width*height</code> elements of a compatible data type.
           @param width  the width of the raster data to be created
           @param height the height of the raster data to be created
           @return raster data compatible with this product raster
           @see #createCompatibleRasterData
           @see #createCompatibleSceneRasterData
        """
        return ProductData(Band_createCompatibleRasterDataForRect(self._obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        """
           Tests whether the given parameters specify a compatible raster or not.
           @param rasterData the raster data
           @param w          the raster width
           @param h          the raster height
           @return {@code true} if so
        """
        return Band_isCompatibleRasterData(self._obj, rasterData._obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        """
           Throws an <code>IllegalArgumentException</code> if the given parameters dont specify a compatible raster.
           @param rasterData the raster data
           @param w          the raster width
           @param h          the raster height
        """
        Band_checkCompatibleRasterData(self._obj, rasterData._obj, w, h)
        return

    def hasIntPixels(self):
        """
           Determines whether this raster data node contains integer samples.
           @return true if this raster data node contains integer samples.
        """
        return Band_hasIntPixels(self._obj)

    def createTransectProfileData(self, shape):
        """
           Creates a transect profile for the given shape (-outline).
           @param shape the shape
           @return the profile data
           @throws IOException if an I/O error occurs
        """
        return TransectProfileData(Band_createTransectProfileData(self._obj, shape._obj))

    def getImageInfo(self):
        """
           Gets the image information for image display.
           @return the image info or null
        """
        return ImageInfo(Band_getImageInfo(self._obj))

    def setImageInfo(self, imageInfo):
        """
           Sets the image information for image display.
           @param imageInfo the image info, can be null
        """
        Band_setImageInfo(self._obj, imageInfo._obj)
        return

    def fireImageInfoChanged(self):
        """
           Notifies listeners that the image (display) information has changed.
        """
        Band_fireImageInfoChanged(self._obj)
        return

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        """
           Creates an instance of a default image information.
           
           An <code>IllegalStateException</code> is thrown in the case that this raster data node has no raster data.
           @param histoSkipAreas the left (at index 0) and right (at index 1) normalized areas of the raster data
           histogram to be excluded when determining the value range for a linear constrast
           stretching. Can be <code>null</code>, in this case <code>{0.01, 0.04}</code> resp. 5% of
           the entire area is skipped.
           @param histogram      the histogram to create the image information.
           @return a valid image information instance, never <code>null</code>.
        """
        return ImageInfo(Band_createDefaultImageInfo(self._obj, histoSkipAreas, histogram._obj))

    def getOverlayMaskGroup(self):
        """
           @return The overlay mask group.
        """
        return ProductNodeGroup(Band_getOverlayMaskGroup(self._obj))

    def createColorIndexedImage(self, pm):
        """
           Creates an image for this raster data node. The method simply returns <code>ProductUtils.createColorIndexedImage(this,
           null)</code>.
           @param pm a monitor to inform the user about progress
           @return a greyscale/palette-based image for this raster data node
           @throws IOException if the raster data is not loaded so far and reload causes an I/O error
           @see #setImageInfo(ImageInfo)
        """
        return BufferedImage(Band_createColorIndexedImage(self._obj, pm._obj))

    def createRgbImage(self, pm):
        """
           Creates an RGB image for this raster data node.
           @param pm a monitor to inform the user about progress
           @return a greyscale/palette-based image for this raster data node
           @throws IOException if the raster data is not loaded so far and reload causes an I/O error
           @see #setImageInfo(ImageInfo)
        """
        return BufferedImage(Band_createRgbImage(self._obj, pm._obj))

    def createPixelValidator(self, lineOffset, roi):
        """
           Creates a validator which can be used to validate indexes of pixels in a flat raster data buffer.
           @param lineOffset the absolute line offset, zero based
           @param roi        an optional ROI
           @return a new validator instance, never null
           @throws IOException if an I/O error occurs
        """
        return IndexValidator(Band_createPixelValidator(self._obj, lineOffset, roi._obj))

    def scale(self, v):
        """
           Applies the scaling <code>v * scalingFactor + scalingOffset</code> the the given input value. If the
           <code>log10Scaled</code> property is true, the result is taken to the power of 10 <i>after</i> the actual
           scaling.
           @param v the input value
           @return the scaled value
        """
        return Band_scale(self._obj, v)

    def scaleInverse(self, v):
        """
           Applies the inverse scaling <code>(v - scalingOffset) / scalingFactor</code> the the given input value. If the
           <code>log10Scaled</code> property is true, the common logarithm is applied to the input <i>before</i> the actual
           scaling.
           @param v the input value
           @return the scaled value
        """
        return Band_scaleInverse(self._obj, v)

    def getPixelString(self, x, y):
        """
           Returns the pixel located at (x,y) as a string value.
           @param x the X co-ordinate of the pixel location
           @param y the Y co-ordinate of the pixel location
           @return the pixel value at (x,y) as string or an error message text
        """
        return Band_getPixelString(self._obj, x, y)

    def isSourceImageSet(self):
        """
           Returns whether the source image is set on this {@code RasterDataNode}.
           @return whether the source image is set.
           @see #getSourceImage()
           @see #setSourceImage(java.awt.image.RenderedImage)
           @see #setSourceImage(com.bc.ceres.glevel.MultiLevelImage)
           @see #createSourceImage()
        """
        return Band_isSourceImageSet(self._obj)

    def getSourceImage(self):
        """
           Gets the source image associated with this {@code RasterDataNode}.
           @return The source image. Never {@code null}. In the case that {@link #isSourceImageSet()} returns {@code false},
           the method {@link #createSourceImage()} will be called in order to set and return a valid source image.
           @see #createSourceImage()
           @see #isSourceImageSet()
        """
        return MultiLevelImage(Band_getSourceImage(self._obj))

    def isGeophysicalImageSet(self):
        """
           Returns whether the geophysical image is set on this {@code RasterDataNode}.
           
           This method belongs to preliminary API and may be removed or changed in the future.
           @return whether the geophysical image is set.
        """
        return Band_isGeophysicalImageSet(self._obj)

    def getGeophysicalImage(self):
        """
           @return The geophysical source image.
        """
        return MultiLevelImage(Band_getGeophysicalImage(self._obj))

    def isValidMaskImageSet(self):
        """
           Returns wether the valid mask image is set on this {@code RasterDataNode}.
           @return Wether the source image is set.
        """
        return Band_isValidMaskImageSet(self._obj)

    def getValidMaskImage(self):
        """
           Gets the valid-mask image associated with this {@code RasterDataNode}.
           @return The rendered image.
        """
        return MultiLevelImage(Band_getValidMaskImage(self._obj))

    def isStxSet(self):
        return Band_isStxSet(self._obj)

    def getStx(self):
        """
           Gets the statistics. If statistcs are not yet available,
           the method will compute (possibly inaccurate) statistics and return those.
           
           If accurate statistics are required, the {@link #getStx(boolean, com.bc.ceres.core.ProgressMonitor)}
           shall be used instead.
           
           This method belongs to preliminary API and may be removed or changed in the future.
           @return The statistics.
           @see #getStx(boolean, com.bc.ceres.core.ProgressMonitor)
           @see #setStx(Stx)
        """
        return Stx(Band_getStx(self._obj))

    def setStx(self, stx):
        """
           Sets the statistics. It is the responsibility of the caller to ensure that the given statistics
           are really related to this {@code RasterDataNode}'s raster data.
           The method fires a property change event for the property {@link #PROPERTY_NAME_STX}.
           This method belongs to preliminary API and may be removed or changed in the future.
           @param stx The statistics.
        """
        Band_setStx(self._obj, stx._obj)
        return

    def getValidShape(self):
        """
           Gets the shape of the area where this raster data contains valid samples.
           The method returns <code>null</code>, if the entire raster contains valid samples.
           @return The shape of the area where the raster data has samples, can be {@code null}.
        """
        return Shape(Band_getValidShape(self._obj))

    def getDataType(self):
        """
           Gets the data type of this data node.
           @return the data type which is always one of the multiple <code>ProductData.TYPE_<i>X</i></code> constants
        """
        return Band_getDataType(self._obj)

    def getNumDataElems(self):
        """
           Gets the number of data elements in this data node.
        """
        return Band_getNumDataElems(self._obj)

    def setData(self, data):
        """
           Sets the data of this data node.
        """
        Band_setData(self._obj, data._obj)
        return

    def getData(self):
        """
           Gets the data of this data node.
        """
        return ProductData(Band_getData(self._obj))

    def setDataElems(self, elems):
        """
           Sets the data elements of this data node.
           @see ProductData#setElems(Object)
        """
        Band_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        """
           Gets the data elements of this data node.
           @see ProductData#getElems()
        """
        return Object(Band_getDataElems(self._obj))

    def getDataElemSize(self):
        """
           Gets the data element size in bytes.
           @see ProductData#getElemSize(int)
        """
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
        """
           Fires a node data changed event. This method is called after the data of this data node changed.
        """
        Band_fireProductNodeDataChanged(self._obj)
        return

    def createCompatibleProductData(self, numElems):
        """
           Creates product data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>numElems</code> elements of a compatible data type.
           @param numElems the number of elements, must not be less than one
           @return product data compatible with this data node
        """
        return ProductData(Band_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(Band_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return Band_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        Band_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return Band_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        Band_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return Band_isModified(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return Band_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(Band_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(Band_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(Band_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return Band_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return Band_getProductRefString(self._obj)


class ColorPaletteDef_Point:
    def __init__(self, obj):
        self._obj = obj


class RenderedImage:
    def __init__(self, obj):
        self._obj = obj


"""
Placemarks are displayed as symbols at the image's pixel position corresponding to their geographical position. The name is
displayed as label next to the symbol. If the user moves the mouse over a placemark, the textual description property shall
appear as tool-tip text. Single placemarks can be selected either by mouse-click or by the ? Prev./Next Placemark tool.
Placemarks are contained in the active product and stored in CSV format. To share placemarks between products,
the placemarks of a product can be imported and exported.
"""
class Placemark:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newPlacemark(descriptor, feature):
        """
           Constructor.
           @param descriptor The placemark descriptor that created this placemark.
           @param feature    The wrapped feature.
        """
        return Placemark(Placemark_newPlacemark(descriptor._obj, feature._obj))

    @staticmethod
    def createPointPlacemark(descriptor, name, label, text, pixelPos, geoPos, geoCoding):
        """
           Creates a point placemark.
           @param descriptor The placemark descriptor that created this placemark.
           @param name       The placemark's name.
           @param label      The placemark's label. May be {@code null}.
           @param text       The placemark's (XHTML) text. May be {@code null}.
           @param pixelPos   The placemark's pixel position. May be {@code null}, if {@code geoPos} is given.
           @param geoPos     The placemark's pixel position. May be {@code null}, if {@code pixelPos} is given.
           @param geoCoding  The placemark's geo-coding. Used to compute {@code pixelPos} from {@code geoPos}, if {@code pixelPos} is {@code null}.
           @return A new point placemark.
        """
        return Placemark(Placemark_createPointPlacemark(descriptor._obj, name, label, text, pixelPos._obj, geoPos._obj, geoCoding._obj))

    def getDescriptor(self):
        """
           @return The placemark descriptor that created this placemark.
        """
        return PlacemarkDescriptor(Placemark_getDescriptor(self._obj))

    def getFeature(self):
        """
           @return The wrapped {@link SimpleFeature} underlying this placemark.
        """
        return SimpleFeature(Placemark_getFeature(self._obj))

    def getAttributeValue(self, attributeName):
        """
           Gets the attribute value of the underlying feature.
           @param attributeName The feature's attribute name.
           @return The feature's attribute value, may be {@code null}.
        """
        return Object(Placemark_getAttributeValue(self._obj, attributeName))

    def setAttributeValue(self, attributeName, attributeValue):
        """
           Sets the attribute value of the underlying feature.
           @param attributeName  The feature's attribute name.
           @param attributeValue The feature's attribute value, may be {@code null}.
        """
        Placemark_setAttributeValue(self._obj, attributeName, attributeValue._obj)
        return

    def setLabel(self, label):
        """
           Sets this placemark's label.
           @param label the label, if {@code null} an empty label is set.
        """
        Placemark_setLabel(self._obj, label)
        return

    def getLabel(self):
        """
           @return This placemark's label, cannot be {@code null}.
        """
        return Placemark_getLabel(self._obj)

    def setText(self, text):
        """
           Sets this placemark's (XHTML) text.
           @param text The text, if {@code null} an empty text is set.
        """
        Placemark_setText(self._obj, text)
        return

    def getText(self):
        """
           @return This placemark's (XHTML) text, cannot be {@code null}.
        """
        return Placemark_getText(self._obj)

    def setStyleCss(self, styleCss):
        """
           Sets this placemark's CSS style.
           @param styleCss The text, if {@code null} an empty text is set.
        """
        Placemark_setStyleCss(self._obj, styleCss)
        return

    def getStyleCss(self):
        """
           @return This placemark's CSS style, cannot be {@code null}.
        """
        return Placemark_getStyleCss(self._obj)

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           @param visitor the visitor
        """
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
        """
           Updates pixel and geo position according to the current geometry (model coordinates).
        """
        Placemark_updatePositions(self._obj)
        return

    @staticmethod
    def createPinFeatureType():
        return SimpleFeatureType(Placemark_createPinFeatureType())

    @staticmethod
    def createGcpFeatureType():
        return SimpleFeatureType(Placemark_createGcpFeatureType())

    @staticmethod
    def createGeometryFeatureType():
        return SimpleFeatureType(Placemark_createGeometryFeatureType())

    @staticmethod
    def createPointFeatureType(name):
        return SimpleFeatureType(Placemark_createPointFeatureType(name))

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(Placemark_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return Placemark_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        Placemark_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return Placemark_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        Placemark_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return Placemark_isModified(self._obj)

    def setModified(self, modified):
        """
           Sets this node's modified flag.
           
           If the modified flag changes to true and this node has an owner, the owner's modified flag is also set to
           true.
           @param modified whether or not this node is beeing marked as modified.
           @see Product#fireNodeChanged
        """
        Placemark_setModified(self._obj, modified)
        return

    def toString(self):
        return Placemark_toString(self._obj)

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        Placemark_dispose(self._obj)
        return

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return Placemark_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(Placemark_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(Placemark_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(Placemark_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return Placemark_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return Placemark_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        Placemark_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
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


"""
The <code>ProductWriterPlugIn</code> interface is implemented by data product writer plug-ins.

XMLCoder plug-ins are used to provide meta-information about a particular data format and to create instances of
the actual writer objects.

 A plug-in can register itself in the <code>ProductIO</code> plug-in registry or it is automatically found during
a classpath scan.
@see ProductReaderPlugIn
"""
class ProductWriterPlugIn:
    def __init__(self, obj):
        self._obj = obj


class File:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>GeoPos</code> class represents a geographical position measured in longitudes and latitudes.
"""
class GeoPos:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newGeoPos(lat, lon):
        """
           Constructs a new geo-position with the given latitude and longitude values.
           @param lat the geographical latitude in decimal degree, valid range is -90 to +90
           @param lon the geographical longitude in decimal degree, valid range is -180 to +180
        """
        return GeoPos(GeoPos_newGeoPos(lat, lon))

    def getLat(self):
        """
           Gets the latitude value.
           @return the geographical latitude in decimal degree
        """
        return GeoPos_getLat(self._obj)

    def getLon(self):
        """
           Gets the longitude value.
           @return the geographical longitude in decimal degree
        """
        return GeoPos_getLon(self._obj)

    def setLocation(self, lat, lon):
        """
           Sets the geographical location of this point.
           @param lat the geographical latitude in decimal degree, valid range is -90 to +90
           @param lon the geographical longitude in decimal degree, valid range is -180 to +180
        """
        GeoPos_setLocation(self._obj, lat, lon)
        return

    def isValid(self):
        """
           Tests whether or not this geo-position is valid.
           @return true, if so
        """
        return GeoPos_isValid(self._obj)

    @staticmethod
    def areValid(a):
        """
           Tests whether or not all given geo-positions are valid.
           @return true, if so
        """
        return GeoPos_areValid(a._obj)

    def setInvalid(self):
        """
           Sets the lat/lon fields so that {@link #isValid()} will return false.
        """
        GeoPos_setInvalid(self._obj)
        return

    def equals(self, obj):
        """
           Indicates whether some other object is "equal to" this one.
           @param obj the reference object with which to compare.
           @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
        """
        return GeoPos_equals(self._obj, obj._obj)

    def hashCode(self):
        """
           Returns a hash code value for the object.
           @return a hash code value for this object.
        """
        return GeoPos_hashCode(self._obj)

    def toString(self):
        """
           Returns a string representation of the object. In general, the <code>toString</code> method returns a string that
           "textually represents" this object.
           @return a string representation of the object.
        """
        return GeoPos_toString(self._obj)

    def normalize(self):
        """
           Normalizes this position so that its longitude is in the range -180 to +180 degree.
        """
        GeoPos_normalize(self._obj)
        return

    @staticmethod
    def normalizeLon(lon):
        """
           Normalizes the given longitude so that it is in the range -180 to +180 degree and returns it.
           Note that -180 will remain as is, although -180 is equivalent to +180 degrees.
           @param lon the longitude in degree
           @return the normalized longitude in the range
        """
        return GeoPos_normalizeLon(lon)

    def getLatString(self):
        """
           Returns a string representation of the latitude value.
           @return a string of the form DDD°[MM'[SS"]] [N|S].
        """
        return GeoPos_getLatString(self._obj)

    def getLonString(self):
        """
           Returns a string representation of the latitude value.
           @return a string of the form DDD°[MM'[SS"]] [W|E].
        """
        return GeoPos_getLonString(self._obj)


"""
A type-safe container for elements of the type <code>ProductNode</code>.
"""
class ProductNodeGroup:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newProductNodeGroup(name):
        """
           Constructs a node group with no owner and which will not take ownership of added children.
           @param name The group name.
        """
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup1(name))

    @staticmethod
    def newProductNodeGroup(owner, name, takingOverNodeOwnership):
        """
           Constructs a node group for the given owner.
           @param owner                   The owner of the group.
           @param name                    The group name.
           @param takingOverNodeOwnership If {@code true}, child nodes will have this group as owner after adding.
        """
        return ProductNodeGroup(ProductNodeGroup_newProductNodeGroup2(owner._obj, name, takingOverNodeOwnership))

    def isTakingOverNodeOwnership(self):
        """
           @return {@code true}, if child nodes will have this group as owner after adding.
        """
        return ProductNodeGroup_isTakingOverNodeOwnership(self._obj)

    def getNodeCount(self):
        """
           @return The number of product nodes in this product group.
        """
        return ProductNodeGroup_getNodeCount(self._obj)

    def get(self, index):
        """
           @param index The node index.
           @return The product node at the given index.
        """
        return T(ProductNodeGroup_get1(self._obj, index))

    def getNodeDisplayNames(self):
        """
           Returns the display names of all products currently managed.
           @return an array containing the display names, never <code>null</code>, but the array can have zero length
           @see ProductNode#getDisplayName()
        """
        return ProductNodeGroup_getNodeDisplayNames(self._obj)

    def getNodeNames(self):
        """
           Returns the names of all products currently managed.
           @return an array containing the names, never <code>null</code>, but the array can have zero length
        """
        return ProductNodeGroup_getNodeNames(self._obj)

    def toArray(self):
        """
           Returns an array of all products currently managed.
           @return an array containing the products, never <code>null</code>, but the array can have zero length
        """
        return ProductNode(ProductNodeGroup_toArray1(self._obj))

    def toArray(self, array):
        """
           @param array the array into which the elements of the list are to be stored, if it is big enough; otherwise, a
           new array of the same runtime type is allocated for this purpose.
           @return an array containing the product nodes, never <code>null</code>, but the array can have zero length
        """
        return T(ProductNodeGroup_toArray2(self._obj, array._obj))

    def indexOf(self, name):
        return ProductNodeGroup_indexOf2(self._obj, name)

    def indexOf(self, element):
        return ProductNodeGroup_indexOf1(self._obj, element._obj)

    def getByDisplayName(self, displayName):
        """
           @param displayName the display name
           @return the product node with the given display name.
        """
        return T(ProductNodeGroup_getByDisplayName(self._obj, displayName))

    def get(self, name):
        """
           @param name the name
           @return the product node with the given name.
        """
        return T(ProductNodeGroup_get2(self._obj, name))

    def contains(self, name):
        """
           Tests whether a node with the given name is contained in this group.
           @param name the name
           @return true, if so
        """
        return ProductNodeGroup_contains2(self._obj, name)

    def contains(self, node):
        """
           Tests whether the given product is contained in this list.
           @param node the node
           @return true, if so
        """
        return ProductNodeGroup_contains1(self._obj, node._obj)

    def add(self, node):
        """
           Adds the given node to this group.
           @param node the node to be added, ignored if <code>null</code>
           @return true, if the node has been added
        """
        return ProductNodeGroup_add2(self._obj, node._obj)

    def add(self, index, node):
        """
           Adds the given node to this group.
           @param index the index.
           @param node  the node to be added, ignored if <code>null</code>
        """
        ProductNodeGroup_add1(self._obj, index, node._obj)
        return

    def remove(self, node):
        """
           Removes the given node from this group.
           @param node the node to be removed
           @return true, if the node was removed
        """
        return ProductNodeGroup_remove(self._obj, node._obj)

    def removeAll(self):
        """
           Removes all nodes from this group.
        """
        ProductNodeGroup_removeAll(self._obj)
        return

    def clearRemovedList(self):
        ProductNodeGroup_clearRemovedList(self._obj)
        return

    def getRemovedNodes(self):
        """
           Gets all removed node nodes.
           @return a collection of all removed node nodes.
        """
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
        """
           @return The owner node of this node.
        """
        return ProductNode(ProductNodeGroup_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return ProductNodeGroup_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        ProductNodeGroup_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return ProductNodeGroup_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        ProductNodeGroup_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return ProductNodeGroup_isModified(self._obj)

    def toString(self):
        return ProductNodeGroup_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return ProductNodeGroup_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(ProductNodeGroup_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(ProductNodeGroup_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(ProductNodeGroup_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return ProductNodeGroup_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return ProductNodeGroup_getProductRefString(self._obj)

    def getRawStorageSize(self):
        """
           Gets an estimated, raw storage size in bytes of this product node.
           @return the size in bytes.
        """
        return ProductNodeGroup_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        ProductNodeGroup_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        ProductNodeGroup_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        ProductNodeGroup_removeFromFile(self._obj, productWriter._obj)
        return


class MapProjection:
    def __init__(self, obj):
        self._obj = obj


"""
A type-safe container for elements of the type <code>Product</code>. ProductListeners can be added to inform if a
<code>Product</code> was added or removed.
"""
class ProductManager:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newProductManager():
        """
           Constructs an product manager with an empty list of products.
        """
        return ProductManager(ProductManager_newProductManager())

    def getProductCount(self):
        """
           @return The number of products in this product manager.
        """
        return ProductManager_getProductCount(self._obj)

    def getProduct(self, index):
        """
           Gets the product at the given index.
           @param index the index
           @return The product at the given index.
        """
        return Product(ProductManager_getProduct(self._obj, index))

    def getProductDisplayNames(self):
        """
           Returns the display names of all products currently managed.
           @return an array containing the display names, never <code>null</code>, but the array can have zero length
           @see ProductNode#getDisplayName()
        """
        return ProductManager_getProductDisplayNames(self._obj)

    def getProductNames(self):
        """
           Returns the names of all products currently managed.
           @return an array containing the names, never <code>null</code>, but the array can have zero length
        """
        return ProductManager_getProductNames(self._obj)

    def getProducts(self):
        """
           Returns an array of all products currently managed.
           @return an array containing the products, never <code>null</code>, but the array can have zero length
        """
        return Product(ProductManager_getProducts(self._obj))

    def getProductByDisplayName(self, displayName):
        """
           @param displayName The product's display name.
           @return The product with the given display name.
        """
        return Product(ProductManager_getProductByDisplayName(self._obj, displayName))

    def getProductByRefNo(self, refNo):
        """
           @param refNo The reference number.
           @return The product with the given reference number.
        """
        return Product(ProductManager_getProductByRefNo(self._obj, refNo))

    def getProduct(self, name):
        """
           @param name The product name.
           @return The product with the given name.
        """
        return Product(ProductManager_getProductByName(self._obj, name))

    def getProductIndex(self, product):
        return ProductManager_getProductIndex(self._obj, product._obj)

    def containsProduct(self, name):
        """
           Tests whether a product with the given name is contained in this list.
           @param name the product name
           @return true, if so
        """
        return ProductManager_containsProduct(self._obj, name)

    def contains(self, product):
        """
           Tests whether the given product is contained in this list.
           @param product The product.
           @return {@code true} if so.
        """
        return ProductManager_contains(self._obj, product._obj)

    def addProduct(self, product):
        """
           Adds the given product to this product manager if it does not already exists and sets it's reference number one
           biger than the greatest reference number in this product manager.
           @param product the product to be added, ignored if <code>null</code>
        """
        ProductManager_addProduct(self._obj, product._obj)
        return

    def removeProduct(self, product):
        """
           Removes the given product from this product manager if it exists.
           @param product the product to be removed, ignored if <code>null</code>
           @return true, if the product was removed
        """
        return ProductManager_removeProduct(self._obj, product._obj)

    def removeAllProducts(self):
        """
           Removes all product from this list.
        """
        ProductManager_removeAllProducts(self._obj)
        return

    def addListener(self, listener):
        """
           Adds a <code>ProductManagerListener</code> to this product manager. The <code>ProductManagerListener</code> is
           informed each time a product was added or removed.
           @param listener the listener to be added.
           @return true if the listener was added, otherwise false.
        """
        return ProductManager_addListener(self._obj, listener._obj)

    def removeListener(self, listener):
        """
           Removes a <code>ProductManagerListener</code> from this product manager.
           @param listener The listener.
           @return true, if the listener was removed, otherwise false.
        """
        return ProductManager_removeListener(self._obj, listener._obj)


"""
Provides the information required to decode integer sample values that
are combined of single flags (bit indexes).
"""
class FlagCoding:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newFlagCoding(name):
        """
           Constructs a new flag coding object with the given name.
           @param name the name
        """
        return FlagCoding(FlagCoding_newFlagCoding(name))

    def getFlag(self, name):
        """
           Returns a metadata attribute wich is the representation of the flag with the given name. This method delegates to
           getPropertyValue(String).
           @param name the flag name
           @return a metadata attribute wich is the representation of the flag with the given name
        """
        return MetadataAttribute(FlagCoding_getFlag(self._obj, name))

    def getFlagNames(self):
        """
           Returns a string array which contains the names of all flags contained in this <code>FlagCoding</code> object.
           @return a string array which contains all names of this <code>FlagCoding</code>.<br> If this
           <code>FlagCoding</code> does not contain any flag, <code>null</code> is returned
        """
        return FlagCoding_getFlagNames(self._obj)

    def addFlag(self, name, flagMask, description):
        """
           Adds a new flag definition to this flags coding.
           @param name        the flag name
           @param flagMask    the flag's bit mask
           @param description the description text
           @throws IllegalArgumentException if <code>name</code> is null
           @return A new attribute representing the flag.
        """
        return MetadataAttribute(FlagCoding_addFlag(self._obj, name, flagMask, description))

    def getFlagMask(self, name):
        """
           Returns the flag mask value for the specified flag name.
           @param name the flag name
           @return flagMask the flag's bit mask as a 32 bit integer
           @throws IllegalArgumentException if <code>name</code> is null, or a flag with the name does not exist
        """
        return FlagCoding_getFlagMask(self._obj, name)

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method simply calls <code>visitor.visit(this)</code>.
           @param visitor the visitor, must not be <code>null</code>
        """
        FlagCoding_acceptVisitor(self._obj, visitor._obj)
        return

    def addElement(self, element):
        """
           Overrides the base class <code>addElement</code> in order to <b>not</b> add an element to this flag coding
           because flag codings do not support inner elements.
           @param element the element to be added, always ignored
        """
        FlagCoding_addElement(self._obj, element._obj)
        return

    def addAttribute(self, attribute):
        """
           Adds an attribute to this node. If an attribute with the same name already exists, the method does nothing.
           @param attribute the attribute to be added
           @throws IllegalArgumentException if the attribute added is not an integer or does not have a scalar value
        """
        FlagCoding_addAttribute(self._obj, attribute._obj)
        return

    def addSample(self, name, value, description):
        """
           Adds a new coding value to this sample coding.
           @param name        the coding name
           @param value       the value
           @param description the description text
           @throws IllegalArgumentException if <code>name</code> is null
           @return A new attribute representing the coded sample.
        """
        return MetadataAttribute(FlagCoding_addSample(self._obj, name, value, description))

    def getSampleCount(self):
        """
           Gets the number of coded sample values.
           @return the number of coded sample values
        """
        return FlagCoding_getSampleCount(self._obj)

    def getSampleName(self, index):
        """
           Gets the sample name at the specified attribute index.
           @param index the attribute index.
           @return the sample name.
        """
        return FlagCoding_getSampleName(self._obj, index)

    def getSampleValue(self, index):
        """
           Gets the sample value at the specified attribute index.
           @param index the attribute index.
           @return the sample value.
        """
        return FlagCoding_getSampleValue(self._obj, index)

    def getElementGroup(self):
        """
           Gets the group of child elements. The method returns null, if this element has no children.
           @return The child element group, may be null.
        """
        return ProductNodeGroup(FlagCoding_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(FlagCoding_getParentElement(self._obj))

    def addElementAt(self, element, index):
        """
           Adds the given element to this element at index.
           @param element the element to added, ignored if <code>null</code>
           @param index   where to put it
        """
        FlagCoding_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        """
           Removes the given element from this element.
           @param element the element to be removed, ignored if <code>null</code>
           @return true, if so
        """
        return FlagCoding_removeElement(self._obj, element._obj)

    def getNumElements(self):
        """
           @return the number of elements contained in this element.
        """
        return FlagCoding_getNumElements(self._obj)

    def getElementAt(self, index):
        """
           Returns the element at the given index.
           @param index the element index
           @return the element at the given index
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return MetadataElement(FlagCoding_getElementAt(self._obj, index))

    def getElementNames(self):
        """
           Returns a string array containing the names of the groups contained in this element
           @return a string array containing the names of the groups contained in this element. If this element has no
           groups a zero-length-array is returned.
        """
        return FlagCoding_getElementNames(self._obj)

    def getElements(self):
        """
           Returns an array of elements contained in this element.
           @return an array of elements contained in this product. If this element has no elements a zero-length-array is
           returned.
        """
        return MetadataElement(FlagCoding_getElements(self._obj))

    def getElement(self, name):
        """
           Returns the element with the given name.
           @param name the element name
           @return the element with the given name or <code>null</code> if a element with the given name is not contained in
           this element.
        """
        return MetadataElement(FlagCoding_getElement(self._obj, name))

    def containsElement(self, name):
        """
           Tests if a element with the given name is contained in this element.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a element with the given name is contained in this element, <code>false</code>
           otherwise
        """
        return FlagCoding_containsElement(self._obj, name)

    def getElementIndex(self, element):
        """
           Gets the index of the given element.
           @param element  The element .
           @return The element's index, or -1.
        """
        return FlagCoding_getElementIndex(self._obj, element._obj)

    def removeAttribute(self, attribute):
        """
           Removes the given attribute from this annotation. If an attribute with the same name already exists, the method
           does nothing.
           @param attribute the attribute to be removed, <code>null</code> is ignored
           @return <code>true</code> if it was removed
        """
        return FlagCoding_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        """
           Returns the number of attributes attaached to this node.
           @return the number of attributes
        """
        return FlagCoding_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        """
           Returns the attribute at the given index.
           @param index the attribute index
           @return the attribute, or <code>null</code> if this node does not contain attributes
           @throws IndexOutOfBoundsException
        """
        return MetadataAttribute(FlagCoding_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        """
           Returns the names of all attributes of this node.
           @return the attribute name array, never <code>null</code>
        """
        return FlagCoding_getAttributeNames(self._obj)

    def getAttributes(self):
        """
           Returns an array of attributes contained in this element.
           @return an array of attributes contained in this product. If this element has no attributes a zero-length-array
           is returned.
        """
        return MetadataAttribute(FlagCoding_getAttributes(self._obj))

    def getAttribute(self, name):
        """
           Returns the attribute with the given name.
           @param name the attribute name
           @return the attribute with the given name or <code>null</code> if it could not be found
        """
        return MetadataAttribute(FlagCoding_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        """
           Checks whether this node has an element with the given name.
           @param name the attribute name
           @return <code>true</code> if so
        """
        return FlagCoding_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        """
           Gets the index of the given attribute.
           @param attribute  The attribute.
           @return The attribute's index, or -1.
        """
        return FlagCoding_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        """
           Returns the double value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as double.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return FlagCoding_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        """
           Returns the UTC value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as UTC.
        """
        return ProductData_UTC(FlagCoding_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        """
           Returns the integer value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return FlagCoding_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        """
           Sets the attribute with the given name to the given integer value. A new attribute with
           <code>ProductData.TYPE_INT32</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        FlagCoding_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        """
           Sets the attribute with the given name to the given double value. A new attribute with
           <code>ProductData.TYPE_FLOAT64</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        FlagCoding_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        """
           Sets the attribute with the given name to the given utc value. A new attribute with
           <code>ProductData.UTC</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        FlagCoding_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        """
           Returns the string value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
        """
        return FlagCoding_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        """
           Sets the attribute with the given name to the given string value. A new attribute with
           <code>ProductData.TYPE_ASCII</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        FlagCoding_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        FlagCoding_setModified(self._obj, modified)
        return

    def createDeepClone(self):
        return MetadataElement(FlagCoding_createDeepClone(self._obj))

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        FlagCoding_dispose(self._obj)
        return

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(FlagCoding_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return FlagCoding_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        FlagCoding_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return FlagCoding_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        FlagCoding_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return FlagCoding_isModified(self._obj)

    def toString(self):
        return FlagCoding_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return FlagCoding_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(FlagCoding_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(FlagCoding_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(FlagCoding_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return FlagCoding_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return FlagCoding_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        FlagCoding_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        FlagCoding_removeFromFile(self._obj, productWriter._obj)
        return


class IndexColorModel:
    def __init__(self, obj):
        self._obj = obj


"""
Enumerates the possible histogram matching modes.
"""
class ImageInfo_HistogramMatching:
    def __init__(self, obj):
        self._obj = obj


"""
A listener which listens to internal data product changes.
"""
class ProductNodeListener:
    def __init__(self, obj):
        self._obj = obj


class Map:
    def __init__(self, obj):
        self._obj = obj


"""
This class provides many static factory methods to be used in conjunction with data products.
@see Product
"""
class ProductUtils:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newProductUtils():
        return ProductUtils(ProductUtils_newProductUtils())

    @staticmethod
    def createImageInfo(rasters, assignMissingImageInfos, pm):
        """
           Creates image creation information.
           @param rasters                 The raster data nodes.
           @param assignMissingImageInfos if {@code true}, it is ensured that to all {@code RasterDataNode}s a valid {@code ImageInfo} will be assigned.
           @param pm                      The progress monitor.
           @return image information
           @throws IOException if an I/O error occurs
        """
        return ImageInfo(ProductUtils_createImageInfo(rasters._obj, assignMissingImageInfos, pm._obj))

    @staticmethod
    def createRgbImage(rasters, imageInfo, pm):
        """
           Creates a RGB image from the given array of <code>{@link RasterDataNode}</code>s.
           The given array <code>rasters</code> containing one or three raster data nodes. If three rasters are given
           RGB image is created, if only one raster is provided a gray scale image created.
           @param rasters   an array of one or three raster nodes.
           @param imageInfo the image info provides the information how to create the image
           @param pm        a monitor to inform the user about progress
           @return the created image
           @throws IOException if the given raster data is not loaded and reload causes an I/O error
           @see RasterDataNode#setImageInfo(ImageInfo)
        """
        return BufferedImage(ProductUtils_createRgbImage(rasters._obj, imageInfo._obj, pm._obj))

    @staticmethod
    def createColorIndexedImage(rasterDataNode, pm):
        """
           Creates a greyscale image from the given <code>{@link RasterDataNode}</code>.
           
           The method uses the given raster data node's image information (an instance of <code>{@link
           ImageInfo}</code>) to create the image.
           @param rasterDataNode the raster data node, must not be <code>null</code>
           @param pm             a monitor to inform the user about progress
           @return the color indexed image
           @throws IOException if the given raster data is not loaded and reload causes an I/O error
           @see RasterDataNode#getImageInfo()
        """
        return BufferedImage(ProductUtils_createColorIndexedImage(rasterDataNode._obj, pm._obj))

    @staticmethod
    def createSuitableMapInfo(product, rect, mapProjection):
        """
           Retuns a suitable <code>MapInfo</code> instance for the given (geo-coded) product which includes the entire or a
           subset of the product's scene region for the given map projection. The position of the reference pixel will be
           the upper left pixel's center (0.5, 0.5).
           @param product       the product, must not be <code>null</code>
           @param rect          the rectangle in pixel coordinates of the product, if <code>null</code> the entire region is
           considered
           @param mapProjection the map projection, must not be <code>null</code>
           @return the map information instance
        """
        return MapInfo(ProductUtils_createSuitableMapInfo1(product._obj, rect._obj, mapProjection._obj))

    @staticmethod
    def createSuitableMapInfo(product, mapProjection, orientation, noDataValue):
        """
           Retuns a suitable <code>MapInfo</code> instance for the given (geo-coded) product which includes the entire or a
           subset of the product's scene region for the given map projection. The position of the reference pixel will be the scene center.
           @param product       the product, must not be <code>null</code>
           @param mapProjection the map projection, must not be <code>null</code>
           @param orientation   the orientation angle
           @param noDataValue   the no-data value to be used
           @return the map information instance
        """
        return MapInfo(ProductUtils_createSuitableMapInfo2(product._obj, mapProjection._obj, orientation, noDataValue))

    @staticmethod
    def getOutputRasterSize(product, rect, mapTransform, pixelSizeX, pixelSizeY):
        return Dimension(ProductUtils_getOutputRasterSize(product._obj, rect._obj, mapTransform._obj, pixelSizeX, pixelSizeY))

    @staticmethod
    def createMapEnvelope(product, rect, mapTransform):
        """
           Creates the boundary in map coordinates for the given product, source rectangle (in product pixel coordinates)
           and the given map transfromation. The method delegates to {@link #createMapEnvelope(Product,
           java.awt.Rectangle, int, org.esa.beam.framework.dataop.maptransf.MapTransform) createMapEnvelope(product, rect,
           step, mapTransform)} where <code>step</code> is the half of the minimum of the product scene raster width and
           height.
           @param product      The product.
           @param rect         The rectangle in pixel coordinates.
           @param mapTransform The map transformation.
           @return The boundary in map coordinates for the given product.
        """
        return Point2D(ProductUtils_createMapEnvelope2(product._obj, rect._obj, mapTransform._obj))

    @staticmethod
    def createMapEnvelope(product, rect, step, mapTransform):
        """
           Creates the boundary in map coordinates for the given product, source rectangle (in product
           pixel coordinates) and the given map transfromation. The method delegates to
           {@link #createMapBoundary(Product, Rectangle, int, MapTransform) createMapBoundary(product, rect,
           step, mapTransform)} where <code>step</code> is the half of the minimum of the product scene
           raster width and height.
           @param product      The product.
           @param rect         The rectangle in pixel coordinates.
           @param step         The step size in pixels.
           @param mapTransform The map transformation.
           @return The boundary in map coordinates for the given product.
        """
        return Point2D(ProductUtils_createMapEnvelope1(product._obj, rect._obj, step, mapTransform._obj))

    @staticmethod
    def getMinMax(boundary):
        return Point2D(ProductUtils_getMinMax(boundary._obj))

    @staticmethod
    def createMapBoundary(product, rect, step, mapTransform):
        return Point2D(ProductUtils_createMapBoundary(product._obj, rect._obj, step, mapTransform._obj))

    @staticmethod
    def createGeoBoundary(product, step):
        """
           Creates the geographical boundary of the given product and returns it as a list of geographical coordinates.
           @param product the input product, must not be null
           @param step    the step given in pixels
           @return an array of geographical coordinates
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
        """
        return GeoPos(ProductUtils_createGeoBoundary1(product._obj, step))

    @staticmethod
    def createGeoBoundary(product, region, step):
        """
           Creates the geographical boundary of the given region within the given product and returns it as a list of
           geographical coordinates.
            This method delegates to {@link #createGeoBoundary(Product, java.awt.Rectangle, int, boolean) createGeoBoundary(Product, Rectangle, int, boolean)}
           and the additional boolean parameter <code>usePixelCenter</code> is <code>true</code>.
           @param product the input product, must not be null
           @param region  the region rectangle in product pixel coordinates, can be null for entire product
           @param step    the step given in pixels
           @return an array of geographical coordinates
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
           @see #createPixelBoundary(RasterDataNode, java.awt.Rectangle, int)
        """
        return GeoPos(ProductUtils_createGeoBoundary2(product._obj, region._obj, step))

    @staticmethod
    def createGeoBoundary(product, region, step, usePixelCenter):
        """
           Creates the geographical boundary of the given region within the given product and returns it as a list of
           geographical coordinates.
           @param product        the input product, must not be null
           @param region         the region rectangle in product pixel coordinates, can be null for entire product
           @param step           the step given in pixels
           @param usePixelCenter <code>true</code> if the pixel center should be used to create the boundary
           @return an array of geographical coordinates
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
           @see #createPixelBoundary(Product, java.awt.Rectangle, int, boolean)
        """
        return GeoPos(ProductUtils_createGeoBoundary3(product._obj, region._obj, step, usePixelCenter))

    @staticmethod
    def createGeoBoundary(raster, region, step):
        """
           Creates the geographical boundary of the given region within the given raster and returns it as a list of
           geographical coordinates.
           @param raster the input raster, must not be null
           @param region the region rectangle in raster pixel coordinates, can be null for entire raster
           @param step   the step given in pixels
           @return an array of geographical coordinates
           @throws IllegalArgumentException if raster is null or if the raster has no {@link GeoCoding} is null
           @see #createPixelBoundary(RasterDataNode, java.awt.Rectangle, int)
        """
        return GeoPos(ProductUtils_createGeoBoundary4(raster._obj, region._obj, step))

    @staticmethod
    def createGeoBoundaryPaths(product):
        """
           Converts the geographic boundary entire product into one, two or three shape objects. If the product does not
           intersect the 180 degree meridian, a single general path is returned. Otherwise two or three shapes are created
           and returned in the order from west to east.
           
           The geographic boundary of the given product are returned as shapes comprising (longitude,latitude) pairs.
           @param product the input product
           @return an array of shape objects
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
           @see #createGeoBoundary(Product, int)
        """
        return GeneralPath(ProductUtils_createGeoBoundaryPaths1(product._obj))

    @staticmethod
    def createGeoBoundaryPaths(product, region, step):
        """
           Converts the geographic boundary of the region within the given product into one, two or three shape objects. If
           the product does not intersect the 180 degree meridian, a single general path is returned. Otherwise two or three
           shapes are created and returned in the order from west to east.
           
           This method delegates to {@link #createGeoBoundaryPaths(Product, java.awt.Rectangle, int, boolean) createGeoBoundaryPaths(Product, Rectangle, int, boolean)}
           and the additional parameter <code>usePixelCenter</code> is <code>true</code>.
           
           The geographic boundary of the given product are returned as shapes comprising (longitude,latitude) pairs.
           @param product the input product
           @param region  the region rectangle in product pixel coordinates, can be null for entire product
           @param step    the step given in pixels
           @return an array of shape objects
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
           @see #createGeoBoundary(Product, java.awt.Rectangle, int)
        """
        return GeneralPath(ProductUtils_createGeoBoundaryPaths2(product._obj, region._obj, step))

    @staticmethod
    def createGeoBoundaryPaths(product, region, step, usePixelCenter):
        """
           Converts the geographic boundary of the region within the given product into one, two or three shape objects. If
           the product does not intersect the 180 degree meridian, a single general path is returned. Otherwise two or three
           shapes are created and returned in the order from west to east.
           
           The geographic boundary of the given product are returned as shapes comprising (longitude,latitude) pairs.
           @param product        the input product
           @param region         the region rectangle in product pixel coordinates, can be null for entire product
           @param step           the step given in pixels
           @param usePixelCenter <code>true</code> if the pixel center should be used to create the pathes
           @return an array of shape objects
           @throws IllegalArgumentException if product is null or if the product's {@link GeoCoding} is null
           @see #createGeoBoundary(Product, java.awt.Rectangle, int, boolean)
        """
        return GeneralPath(ProductUtils_createGeoBoundaryPaths3(product._obj, region._obj, step, usePixelCenter))

    @staticmethod
    def createPixelBoundary(product, rect, step):
        """
           Creates a rectangular boundary expressed in pixel positions for the given source rectangle. If the source
           <code>rect</code> is 100 x 50 pixels and <code>step</code> is 10 the returned array will countain exactly 2 * 10
           + 2 * (5 - 2) = 26 pixel positions.
           
           This method is used for an intermediate step when determining a product boundary expressed in geographical
           co-ordinates.
            This method delegates to {@link #createPixelBoundary(Product, java.awt.Rectangle, int, boolean) createPixelBoundary(Product, Rectangle, int, boolean)}
           and the additional boolean parameter <code>usePixelCenter</code> is <code>true</code>.
           @param product the product
           @param rect    the source rectangle
           @param step    the mean distance from one pixel position to the other in the returned array
           @return the rectangular boundary
        """
        return PixelPos(ProductUtils_createPixelBoundary1(product._obj, rect._obj, step))

    @staticmethod
    def createPixelBoundary(product, rect, step, usePixelCenter):
        """
           Creates a rectangular boundary expressed in pixel positions for the given source rectangle. If the source
           <code>rect</code> is 100 x 50 pixels and <code>step</code> is 10 the returned array will countain exactly 2 * 10
           + 2 * (5 - 2) = 26 pixel positions.
           
           This method is used for an intermediate step when determining a product boundary expressed in geographical
           co-ordinates.
           @param product        the product
           @param rect           the source rectangle
           @param step           the mean distance from one pixel position to the other in the returned array
           @param usePixelCenter <code>true</code> if the pixel center should be used to create the boundary
           @return the rectangular boundary
        """
        return PixelPos(ProductUtils_createPixelBoundary2(product._obj, rect._obj, step, usePixelCenter))

    @staticmethod
    def createPixelBoundary(raster, rect, step):
        """
           Creates a rectangular boundary expressed in pixel positions for the given source rectangle. If the source
           <code>rect</code> is 100 x 50 pixels and <code>step</code> is 10 the returned array will countain exactly 2 * 10
           + 2 * (5 - 2) = 26 pixel positions.
           
           This method is used for an intermediate step when determining a raster boundary expressed in geographical
           co-ordinates.
           @param raster the raster
           @param rect   the source rectangle
           @param step   the mean distance from one pixel position to the other in the returned array
           @return the rectangular boundary
        """
        return PixelPos(ProductUtils_createPixelBoundary3(raster._obj, rect._obj, step))

    @staticmethod
    def createRectBoundary(rect, step):
        """
           Creates a rectangular boundary expressed in pixel positions for the given source rectangle. If the source
           <code>rect</code> is 100 x 50 pixels and <code>step</code> is 10 the returned array will countain exactly 2 * 10
           + 2 * (5 - 2) = 26 pixel positions.
           This method is used for an intermediate step when determining a product boundary expressed in geographical
           co-ordinates.
            This method delegates to {@link #createRectBoundary(java.awt.Rectangle, int, boolean) createRectBoundary(Rectangle, int, boolean)}
           and the additional boolean parameter <code>usePixelCenter</code> is <code>true</code>.
           @param rect the source rectangle
           @param step the mean distance from one pixel position to the other in the returned array
           @return the rectangular boundary
        """
        return PixelPos(ProductUtils_createRectBoundary1(rect._obj, step))

    @staticmethod
    def createRectBoundary(rect, step, usePixelCenter):
        """
           Creates a rectangular boundary expressed in pixel positions for the given source rectangle. If the source
           <code>rect</code> is 100 x 50 pixels and <code>step</code> is 10 the returned array will countain exactly 2 * 10
           + 2 * (5 - 2) = 26 pixel positions.
           
           This method is used for an intermediate step when determining a product boundary expressed in geographical
           co-ordinates.
           
           @param rect           the source rectangle
           @param step           the mean distance from one pixel position to the other in the returned array
           @param usePixelCenter <code>true</code> if the pixel center should be used
           @return the rectangular boundary
        """
        return PixelPos(ProductUtils_createRectBoundary2(rect._obj, step, usePixelCenter))

    @staticmethod
    def copyFlagCodings(source, target):
        """
           Copies the flag codings from the source product to the target.
           @param source the source product
           @param target the target product
        """
        ProductUtils_copyFlagCodings(source._obj, target._obj)
        return

    @staticmethod
    def copyFlagCoding(sourceFlagCoding, target):
        """
           Copies the given source flag coding to the target product.
           If it exists already, the method simply returns the existing instance.
           @param sourceFlagCoding the source flag coding
           @param target           the target product
           @return The flag coding.
        """
        return FlagCoding(ProductUtils_copyFlagCoding(sourceFlagCoding._obj, target._obj))

    @staticmethod
    def copyIndexCoding(sourceIndexCoding, target):
        """
           Copies the given source index coding to the target product
           If it exists already, the method simply returns the existing instance.
           @param sourceIndexCoding the source index coding
           @param target            the target product
           @return The index coding.
        """
        return IndexCoding(ProductUtils_copyIndexCoding(sourceIndexCoding._obj, target._obj))

    @staticmethod
    def copyMasks(sourceProduct, targetProduct):
        """
           Copies the {@link Mask}s from the source product to the target product.
           
           IMPORTANT NOTE: This method should only be used, if it is known that all masks
           in the source product will also be valid in the target product. This method does
           <em>not</em> copy overlay masks from the source bands to the target bands. Also
           note that a source mask is not copied to the target product, when there already
           is a mask in the target product with the same name as the source mask.
           @param sourceProduct the source product
           @param targetProduct the target product
        """
        ProductUtils_copyMasks(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def copyOverlayMasks(sourceProduct, targetProduct):
        """
           Copies the overlay {@link Mask}s from the source product's raster data nodes to
           the target product's raster data nodes.
           
           IMPORTANT NOTE: This method should only be used, if it is known that all masks
           in the source product will also be valid in the target product. This method does
           <em>not</em> copy overlay masks, which are not contained in the target product's
           mask group.
           @param sourceProduct the source product
           @param targetProduct the target product
        """
        ProductUtils_copyOverlayMasks(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def copyFlagBands(sourceProduct, targetProduct, copySourceImage):
        """
           Copies all bands which contain a flagcoding from the source product to the target product.
           @param sourceProduct   the source product
           @param targetProduct   the target product
           @param copySourceImage whether the source image of the source band should be copied.
        """
        ProductUtils_copyFlagBands(sourceProduct._obj, targetProduct._obj, copySourceImage)
        return

    @staticmethod
    def copyTiePointGrid(gridName, sourceProduct, targetProduct):
        """
           Copies the named tie-point grid from the source product to the target product.
           @param gridName      the name of the tie-point grid to be copied.
           @param sourceProduct the source product
           @param targetProduct the target product
           @return the copied tie-point grid, or <code>null</code> if the sourceProduct does not contain a tie-point grid with the given name.
        """
        return TiePointGrid(ProductUtils_copyTiePointGrid(gridName, sourceProduct._obj, targetProduct._obj))

    @staticmethod
    def copyBand(sourceBandName, sourceProduct, targetProduct, copySourceImage):
        """
           Copies the named band from the source product to the target product.
           @param sourceBandName  the name of the band to be copied.
           @param sourceProduct   the source product.
           @param targetProduct   the target product.
           @param copySourceImage whether the source image of the source band should be copied.
           @return the copy of the band, or <code>null</code> if the sourceProduct does not contain a band with the given name.
        """
        return Band(ProductUtils_copyBand2(sourceBandName, sourceProduct._obj, targetProduct._obj, copySourceImage))

    @staticmethod
    def copyBand(sourceBandName, sourceProduct, targetBandName, targetProduct, copySourceImage):
        """
           Copies the named band from the source product to the target product.
           @param sourceBandName  the name of the band to be copied.
           @param sourceProduct   the source product.
           @param targetBandName  the name of the band copied.
           @param targetProduct   the target product.
           @param copySourceImage whether the source image of the source band should be copied.
           @return the copy of the band, or <code>null</code> if the sourceProduct does not contain a band with the given name.
        """
        return Band(ProductUtils_copyBand1(sourceBandName, sourceProduct._obj, targetBandName, targetProduct._obj, copySourceImage))

    @staticmethod
    def copyRasterDataNodeProperties(sourceRaster, targetRaster):
        """
           Copies all properties from source band to the target band.
           @param sourceRaster the source band
           @param targetRaster the target band
           @see #copySpectralBandProperties(Band, Band)
        """
        ProductUtils_copyRasterDataNodeProperties(sourceRaster._obj, targetRaster._obj)
        return

    @staticmethod
    def copySpectralBandProperties(sourceBand, targetBand):
        """
           Copies the spectral properties from source band to target band. These properties are:
           <ul>
           <li>{@link Band#getSpectralBandIndex() spectral band index},</li>
           <li>{@link Band#getSpectralWavelength() the central wavelength},</li>
           <li>{@link Band#getSpectralBandwidth() the spectral bandwidth} and</li>
           <li>{@link Band#getSolarFlux() the solar spectral flux}.</li>
           </ul>
           @param sourceBand the source band
           @param targetBand the target band
           @see #copyRasterDataNodeProperties(RasterDataNode, RasterDataNode)
        """
        ProductUtils_copySpectralBandProperties(sourceBand._obj, targetBand._obj)
        return

    @staticmethod
    def copyGeoCoding(sourceProduct, targetProduct):
        """
           Copies the geocoding from the source product to target product.
           @param sourceProduct the source product
           @param targetProduct the target product
           @throws IllegalArgumentException if one of the params is <code>null</code>.
        """
        ProductUtils_copyGeoCoding(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def copyTiePointGrids(sourceProduct, targetProduct):
        """
           Copies all tie point grids from one product to another.
           @param sourceProduct the source product
           @param targetProduct the target product
        """
        ProductUtils_copyTiePointGrids(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def copyVectorData(sourceProduct, targetProduct):
        ProductUtils_copyVectorData(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def canGetPixelPos(product):
        """
           Returns whether or not a product can return a pixel position from a given geographical position.
           @param product the product to be checked
           @return <code>true</code> if the given product can return a pixel position
        """
        return ProductUtils_canGetPixelPos1(product._obj)

    @staticmethod
    def canGetPixelPos(raster):
        """
           Returns whether or not a raster can return a pixel position from a given geographical position.
           @param raster the raster to be checked
           @return <code>true</code> if the given raster can return a pixel position
        """
        return ProductUtils_canGetPixelPos2(raster._obj)

    @staticmethod
    def createDensityPlotImage(raster1, sampleMin1, sampleMax1, raster2, sampleMin2, sampleMax2, roiMask, width, height, background, image, pm):
        """
           Creates a density plot image from two raster data nodes.
           @param raster1    the first raster data node
           @param sampleMin1 the minimum sample value to be considered in the first raster
           @param sampleMax1 the maximum sample value to be considered in the first raster
           @param raster2    the second raster data node
           @param sampleMin2 the minimum sample value to be considered in the second raster
           @param sampleMax2 the maximum sample value to be considered in the second raster
           @param roiMask    an optional mask to be used as a ROI for the computation
           @param width      the width of the output image
           @param height     the height of the output image
           @param background the background color of the output image
           @param image      an image to be used as output image, if <code>null</code> a new image is created
           @param pm         the progress monitor
           @return the density plot image
           @throws java.io.IOException when an error occurred.
        """
        return BufferedImage(ProductUtils_createDensityPlotImage(raster1._obj, sampleMin1, sampleMax1, raster2._obj, sampleMin2, sampleMax2, roiMask._obj, width, height, background._obj, image._obj, pm._obj))

    @staticmethod
    def overlayMasks(raster, overlayBIm, pm):
        """
           Draws all the masks contained overlay mask group of the given raster to the ovelayBIm image.
           @param raster     the raster data node which contains all the activated bitmask definitions
           @param overlayBIm the source image which is used as base image for all the overlays.
           @param pm         a monitor to inform the user about progress
           @return the modified given overlayBImm which contains all the activated masks.
           @see RasterDataNode#getOverlayMaskGroup()
        """
        return BufferedImage(ProductUtils_overlayMasks(raster._obj, overlayBIm._obj, pm._obj))

    @staticmethod
    def getCenterGeoPos(product):
        return GeoPos(ProductUtils_getCenterGeoPos(product._obj))

    @staticmethod
    def normalizeGeoPolygon(polygon):
        """
           Normalizes the given geographical polygon so that maximum longitude differences between two points are 180
           degrees. The method operates only on the longitude values of the given polygon.
           @param polygon a geographical, closed polygon
           @return 0 if normalizing has not been applied , -1 if negative normalizing has been applied, 1 if positive
           normalizing has been applied, 2 if positive and negative normalising has been applied
           @see #denormalizeGeoPolygon(GeoPos[])
        """
        return ProductUtils_normalizeGeoPolygon(polygon._obj)

    @staticmethod
    def normalizeGeoPolygon_old(polygon):
        return ProductUtils_normalizeGeoPolygon_old(polygon._obj)

    @staticmethod
    def denormalizeGeoPolygon(polygon):
        """
           Denormalizes the longitude values which have been normalized using the
           {@link #normalizeGeoPolygon(GeoPos[])} method. The
           method operates only on the longitude values of the given polygon.
           @param polygon a geographical, closed polygon
        """
        ProductUtils_denormalizeGeoPolygon(polygon._obj)
        return

    @staticmethod
    def denormalizeGeoPos(geoPos):
        ProductUtils_denormalizeGeoPos(geoPos._obj)
        return

    @staticmethod
    def denormalizeGeoPos_old(geoPos):
        ProductUtils_denormalizeGeoPos_old(geoPos._obj)
        return

    @staticmethod
    def getRotationDirection(polygon):
        return ProductUtils_getRotationDirection(polygon._obj)

    @staticmethod
    def getAngleSum(polygon):
        return ProductUtils_getAngleSum(polygon._obj)

    @staticmethod
    def convertToPixelPath(geoPath, geoCoding):
        """
           Converts a <code>GeneralPath</code> given in geographic lon/lat coordinates into a <code>GeneralPath</code> in
           pixel coordinates using the supplied geo coding.
           @param geoPath   a <code>GeneralPath</code> given in geographic lon/lat coordinates, as returned by the {@link
           #convertToGeoPath(Shape, GeoCoding)} method
           @param geoCoding the geocoding used to convert the geographic coordinates into pixel coordinates.
           @return a <code>GeneralPath</code> given in pixel coordinates.
           @throws IllegalArgumentException if one of the given parameter is null.
           @throws IllegalStateException    if the given geoPath is not a geo referenced <code>GeneralPath</code> wich
           contains only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types.
           @see #convertToGeoPath(Shape, GeoCoding)
        """
        return GeneralPath(ProductUtils_convertToPixelPath(geoPath._obj, geoCoding._obj))

    @staticmethod
    def convertToGeoPath(shape, geoCoding):
        """
           Converts a <code>Shape</code> given in pixel X/Y coordinates into a <code>GeneralPath</code> in geografic
           coordinates using the supplied geo coding.
           @param shape     a <code>Shape</code> given in pixel X/Y coordinates
           @param geoCoding the geo coding used to convert the pixel coordinates into geografic coordinates.
           @return a <code>GeneralPath</code> given in geografic coordinates
           @throws IllegalArgumentException if one of the given parameter is <code>null</code> or the given geo coding can
           not get geografic coordinates.
           @throws IllegalStateException    if this method was used with a java runtime version in which it is not guaranted
           that a <code>PathIterator</code> returned by {@link Shape#getPathIterator(java.awt.geom.AffineTransform,
           double)} returnes only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types.
           @see GeoCoding#canGetGeoPos()
        """
        return GeneralPath(ProductUtils_convertToGeoPath(shape._obj, geoCoding._obj))

    @staticmethod
    def copyMetadata(source, target):
        """
           Copies all metadata elements and attributes of the source product to the target product.
           The copied elements and attributes are deeply cloned.
           @param source the source product.
           @param target the target product.
           @throws NullPointerException if the source or the target product is {@code null}.
        """
        ProductUtils_copyMetadata2(source._obj, target._obj)
        return

    @staticmethod
    def copyMetadata(source, target):
        """
           Copies all metadata elements and attributes of the source element to the target element.
           The copied elements and attributes are deeply cloned.
           @param source the source element.
           @param target the target element.
           @throws NullPointerException if the source or the target element is {@code null}.
        """
        ProductUtils_copyMetadata1(source._obj, target._obj)
        return

    @staticmethod
    def copyPreferredTileSize(sourceProduct, targetProduct):
        """
           Copies the source product's preferred tile size (if any) to the target product.
           @param sourceProduct The source product.
           @param targetProduct The target product.
        """
        ProductUtils_copyPreferredTileSize(sourceProduct._obj, targetProduct._obj)
        return

    @staticmethod
    def createGeoTIFFMetadata(product):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata2(product._obj))

    @staticmethod
    def createGeoTIFFMetadata(geoCoding, width, height):
        return GeoTIFFMetadata(ProductUtils_createGeoTIFFMetadata1(geoCoding._obj, width, height))

    @staticmethod
    def areaToPath(negativeArea, deltaX):
        return GeneralPath(ProductUtils_areaToPath(negativeArea._obj, deltaX))

    @staticmethod
    def addElementToHistory(product, elem):
        """
           Adds a given elem to the history of the given product. If the products metadata root
           does not contain a history entry a new one will be created.
           @param product the product to add the history element.
           @param elem    the element to add to the products history. If <code>null</code> nothing will be added.
        """
        ProductUtils_addElementToHistory(product._obj, elem._obj)
        return

    @staticmethod
    def removeInvalidExpressions(product):
        """
           Validates all the expressions contained in the given (output) product. If an expression is not applicable to the given
           product, the related element is removed.
           @param product the (output) product to be cleaned up
           @return an array of messages which changes are done to the given product.
        """
        return ProductUtils_removeInvalidExpressions(product._obj)

    @staticmethod
    def findSuitableQuicklookBandName(product):
        """
           Finds the name of a band in the given product which is suitable to product a good quicklook.
           The method prefers bands with longer wavelengths, in order to produce good results for night-time scenes.
           @param product the product to be searched
           @return the name of a suitable band or null if the given product does not contain any bands
        """
        return ProductUtils_findSuitableQuicklookBandName(product._obj)

    @staticmethod
    def computeSourcePixelCoordinates(sourceGeoCoding, sourceWidth, sourceHeight, destGeoCoding, destArea):
        return PixelPos(ProductUtils_computeSourcePixelCoordinates(sourceGeoCoding._obj, sourceWidth, sourceHeight, destGeoCoding._obj, destArea._obj))

    @staticmethod
    def computeMinMaxY(pixelPositions):
        """
           Computes the minimum and maximum y value of the given {@link PixelPos} array.
           @param pixelPositions the {@link PixelPos} array
           @return an int array which containes the minimum and maximum y value of the given {@link PixelPos} array in the
           order:<br> &nbsp;&nbsp;&nbsp;&nbsp;[0] - the minimum value<br>&nbsp;&nbsp;&nbsp;&nbsp;[1] - the maximum
           value<br><br>or <code>null</code> if no minimum or maximum can be retrieved because there given array is
           empty.
           @throws IllegalArgumentException if the given pixelPositions are <code>null</code>.
        """
        return ProductUtils_computeMinMaxY(pixelPositions._obj)

    @staticmethod
    def copyBandsForGeomTransform(sourceProduct, targetProduct, defaultNoDataValue, addedRasterDataNodes):
        """
           Copies only the bands from source to target.
           @see #copyBandsForGeomTransform(Product, Product, boolean, double, java.util.Map)
        """
        ProductUtils_copyBandsForGeomTransform1(sourceProduct._obj, targetProduct._obj, defaultNoDataValue, addedRasterDataNodes._obj)
        return

    @staticmethod
    def copyBandsForGeomTransform(sourceProduct, targetProduct, includeTiePointGrids, defaultNoDataValue, targetToSourceMap):
        """
           Adds raster data nodes of a source product as bands to the given target product. This method is especially usefull if the target
           product is a geometric transformation (e.g. map-projection) of the source product.
           If
           {@link RasterDataNode#isScalingApplied() sourceBand.scalingApplied} is true,
           this method will always create the related target band with the raw data type {@link ProductData#TYPE_FLOAT32},
           regardless which raw data type the source band has.
           In this case, {@link RasterDataNode#getScalingFactor() targetBand.scalingFactor}
           will always be 1.0, {@link RasterDataNode#getScalingOffset() targetBand.scalingOffset}
           will always be 0.0 and
           {@link RasterDataNode#isLog10Scaled() targetBand.log10Scaled} will be taken from the source band.
           This ensures that source pixel resampling methods operating on floating point
           data can be stored without loss in accuracy in the target band.
           
           Furthermore, the
           {@link RasterDataNode#isNoDataValueSet() targetBands.noDataValueSet}
           and {@link RasterDataNode#isNoDataValueUsed() targetBands.noDataValueUsed}
           properties will always be true for all added target bands. The {@link RasterDataNode#getGeophysicalNoDataValue() targetBands.geophysicalNoDataValue},
           will be either the one from the source band, if any, or otherwise the one passed into this method.
           @param sourceProduct        the source product as the source for the band specifications. Must be not
           <code>null</code>.
           @param targetProduct        the destination product to receive the bands created. Must be not <code>null</code>.
           @param includeTiePointGrids if {@code true}, tie-point grids of source product will be included as bands in target product
           @param defaultNoDataValue   the default, geophysical no-data value to be used if no no-data value is used by the source band.
           @param targetToSourceMap    a mapping from a target band to a source raster data node, can be {@code null}
        """
        ProductUtils_copyBandsForGeomTransform2(sourceProduct._obj, targetProduct._obj, includeTiePointGrids, defaultNoDataValue, targetToSourceMap._obj)
        return

    @staticmethod
    def getScanLineTime(product, y):
        return ProductData_UTC(ProductUtils_getScanLineTime(product._obj, y))

    @staticmethod
    def getGeophysicalSampleDouble(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleDouble(band._obj, pixelX, pixelY, level)

    @staticmethod
    def getGeophysicalSampleLong(band, pixelX, pixelY, level):
        return ProductUtils_getGeophysicalSampleLong(band._obj, pixelX, pixelY, level)


"""
A <code>MetadataElement</code> is a data node used to store metadata. Metadata elements can have any number of
metadata attributes of the type {@link MetadataAttribute} and any number of inner <code>MetadataElement</code>s.
"""
class MetadataElement:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newMetadataElement(name):
        """
           Constructs a new metadata element.
           @param name the element name
        """
        return MetadataElement(MetadataElement_newMetadataElement(name))

    def getElementGroup(self):
        """
           Gets the group of child elements. The method returns null, if this element has no children.
           @return The child element group, may be null.
        """
        return ProductNodeGroup(MetadataElement_getElementGroup(self._obj))

    def getParentElement(self):
        return MetadataElement(MetadataElement_getParentElement(self._obj))

    def addElement(self, element):
        """
           Adds the given element to this element.
           @param element the element to added, ignored if <code>null</code>
        """
        MetadataElement_addElement(self._obj, element._obj)
        return

    def addElementAt(self, element, index):
        """
           Adds the given element to this element at index.
           @param element the element to added, ignored if <code>null</code>
           @param index   where to put it
        """
        MetadataElement_addElementAt(self._obj, element._obj, index)
        return

    def removeElement(self, element):
        """
           Removes the given element from this element.
           @param element the element to be removed, ignored if <code>null</code>
           @return true, if so
        """
        return MetadataElement_removeElement(self._obj, element._obj)

    def getNumElements(self):
        """
           @return the number of elements contained in this element.
        """
        return MetadataElement_getNumElements(self._obj)

    def getElementAt(self, index):
        """
           Returns the element at the given index.
           @param index the element index
           @return the element at the given index
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return MetadataElement(MetadataElement_getElementAt(self._obj, index))

    def getElementNames(self):
        """
           Returns a string array containing the names of the groups contained in this element
           @return a string array containing the names of the groups contained in this element. If this element has no
           groups a zero-length-array is returned.
        """
        return MetadataElement_getElementNames(self._obj)

    def getElements(self):
        """
           Returns an array of elements contained in this element.
           @return an array of elements contained in this product. If this element has no elements a zero-length-array is
           returned.
        """
        return MetadataElement(MetadataElement_getElements(self._obj))

    def getElement(self, name):
        """
           Returns the element with the given name.
           @param name the element name
           @return the element with the given name or <code>null</code> if a element with the given name is not contained in
           this element.
        """
        return MetadataElement(MetadataElement_getElement(self._obj, name))

    def containsElement(self, name):
        """
           Tests if a element with the given name is contained in this element.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a element with the given name is contained in this element, <code>false</code>
           otherwise
        """
        return MetadataElement_containsElement(self._obj, name)

    def getElementIndex(self, element):
        """
           Gets the index of the given element.
           @param element  The element .
           @return The element's index, or -1.
        """
        return MetadataElement_getElementIndex(self._obj, element._obj)

    def addAttribute(self, attribute):
        """
           Adds an attribute to this node.
           @param attribute the attribute to be added, <code>null</code> is ignored
        """
        MetadataElement_addAttribute(self._obj, attribute._obj)
        return

    def removeAttribute(self, attribute):
        """
           Removes the given attribute from this annotation. If an attribute with the same name already exists, the method
           does nothing.
           @param attribute the attribute to be removed, <code>null</code> is ignored
           @return <code>true</code> if it was removed
        """
        return MetadataElement_removeAttribute(self._obj, attribute._obj)

    def getNumAttributes(self):
        """
           Returns the number of attributes attaached to this node.
           @return the number of attributes
        """
        return MetadataElement_getNumAttributes(self._obj)

    def getAttributeAt(self, index):
        """
           Returns the attribute at the given index.
           @param index the attribute index
           @return the attribute, or <code>null</code> if this node does not contain attributes
           @throws IndexOutOfBoundsException
        """
        return MetadataAttribute(MetadataElement_getAttributeAt(self._obj, index))

    def getAttributeNames(self):
        """
           Returns the names of all attributes of this node.
           @return the attribute name array, never <code>null</code>
        """
        return MetadataElement_getAttributeNames(self._obj)

    def getAttributes(self):
        """
           Returns an array of attributes contained in this element.
           @return an array of attributes contained in this product. If this element has no attributes a zero-length-array
           is returned.
        """
        return MetadataAttribute(MetadataElement_getAttributes(self._obj))

    def getAttribute(self, name):
        """
           Returns the attribute with the given name.
           @param name the attribute name
           @return the attribute with the given name or <code>null</code> if it could not be found
        """
        return MetadataAttribute(MetadataElement_getAttribute(self._obj, name))

    def containsAttribute(self, name):
        """
           Checks whether this node has an element with the given name.
           @param name the attribute name
           @return <code>true</code> if so
        """
        return MetadataElement_containsAttribute(self._obj, name)

    def getAttributeIndex(self, attribute):
        """
           Gets the index of the given attribute.
           @param attribute  The attribute.
           @return The attribute's index, or -1.
        """
        return MetadataElement_getAttributeIndex(self._obj, attribute._obj)

    def getAttributeDouble(self, name, defaultValue):
        """
           Returns the double value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as double.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return MetadataElement_getAttributeDouble(self._obj, name, defaultValue)

    def getAttributeUTC(self, name, defaultValue):
        """
           Returns the UTC value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as UTC.
        """
        return ProductData_UTC(MetadataElement_getAttributeUTC(self._obj, name, defaultValue._obj))

    def getAttributeInt(self, name, defaultValue):
        """
           Returns the integer value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
           @throws NumberFormatException if the attribute type is ASCII but cannot be converted to a number
        """
        return MetadataElement_getAttributeInt(self._obj, name, defaultValue)

    def setAttributeInt(self, name, value):
        """
           Sets the attribute with the given name to the given integer value. A new attribute with
           <code>ProductData.TYPE_INT32</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        MetadataElement_setAttributeInt(self._obj, name, value)
        return

    def setAttributeDouble(self, name, value):
        """
           Sets the attribute with the given name to the given double value. A new attribute with
           <code>ProductData.TYPE_FLOAT64</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        MetadataElement_setAttributeDouble(self._obj, name, value)
        return

    def setAttributeUTC(self, name, value):
        """
           Sets the attribute with the given name to the given utc value. A new attribute with
           <code>ProductData.UTC</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        MetadataElement_setAttributeUTC(self._obj, name, value._obj)
        return

    def getAttributeString(self, name, defaultValue):
        """
           Returns the string value of the attribute with the given name. The given default value is returned if an
           attribute with the given name could not be found in this node.
           @param name         the attribute name
           @param defaultValue the default value
           @return the attribute value as integer.
        """
        return MetadataElement_getAttributeString(self._obj, name, defaultValue)

    def setAttributeString(self, name, value):
        """
           Sets the attribute with the given name to the given string value. A new attribute with
           <code>ProductData.TYPE_ASCII</code> is added to this node if an attribute with the given name could not be found
           in this node.
           @param name  the attribute name
           @param value the new value
        """
        MetadataElement_setAttributeString(self._obj, name, value)
        return

    def setModified(self, modified):
        MetadataElement_setModified(self._obj, modified)
        return

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method first visits (calls <code>acceptVisitor</code> for) all elements contained in this element and then
           visits all attributes. Finally the method calls <code>visitor.visit(this)</code>.
           @param visitor the visitor
        """
        MetadataElement_acceptVisitor(self._obj, visitor._obj)
        return

    def createDeepClone(self):
        return MetadataElement(MetadataElement_createDeepClone(self._obj))

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        MetadataElement_dispose(self._obj)
        return

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(MetadataElement_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return MetadataElement_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        MetadataElement_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return MetadataElement_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        MetadataElement_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return MetadataElement_isModified(self._obj)

    def toString(self):
        return MetadataElement_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return MetadataElement_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(MetadataElement_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(MetadataElement_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(MetadataElement_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return MetadataElement_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return MetadataElement_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        MetadataElement_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        MetadataElement_removeFromFile(self._obj, productWriter._obj)
        return


"""
The interface <code>Pointing</code> wraps a {@link GeoCoding} and optionally provides more geometry
information such as sun direction, satellite (view) direction and elevation at a given pixel position.

All <code>Pointing</code> implementations should override
the {@link Object#equals(Object) equals()} and  {@link Object#hashCode() hashCode()} methods.
"""
class Pointing:
    def __init__(self, obj):
        self._obj = obj


class Color:
    def __init__(self, obj):
        self._obj = obj


"""
Placemark descriptors are used to describe and create {@link Placemark}s.

New placemark descriptors can be added by using the Service Provider Interface
{@code META-INF/services/PlacemarkDescriptor}.

Since this interface is likely to change, clients should not directly implement it.
Instead they should derive their implementation from {@link AbstractPlacemarkDescriptor}.
"""
class PlacemarkDescriptor:
    def __init__(self, obj):
        self._obj = obj


"""
A factory which creates instances of a {@link Pointing} for a given raster data node.
A <code>PointingFactory</code> is usually assigned to data {@link Product} by its {@link ProductReader ProductReader}
"""
class PointingFactory:
    def __init__(self, obj):
        self._obj = obj


"""
A container for data which fully describes a transect profile. Use {@link TransectProfileDataBuilder} to create
instances.
"""
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
        """
           @return {@code true}, if child nodes will have this group as owner after adding.
        """
        return PlacemarkGroup_isTakingOverNodeOwnership(self._obj)

    def getNodeCount(self):
        """
           @return The number of product nodes in this product group.
        """
        return PlacemarkGroup_getNodeCount(self._obj)

    def get(self, index):
        """
           @param index The node index.
           @return The product node at the given index.
        """
        return T(PlacemarkGroup_get1(self._obj, index))

    def getNodeDisplayNames(self):
        """
           Returns the display names of all products currently managed.
           @return an array containing the display names, never <code>null</code>, but the array can have zero length
           @see ProductNode#getDisplayName()
        """
        return PlacemarkGroup_getNodeDisplayNames(self._obj)

    def getNodeNames(self):
        """
           Returns the names of all products currently managed.
           @return an array containing the names, never <code>null</code>, but the array can have zero length
        """
        return PlacemarkGroup_getNodeNames(self._obj)

    def toArray(self):
        """
           Returns an array of all products currently managed.
           @return an array containing the products, never <code>null</code>, but the array can have zero length
        """
        return ProductNode(PlacemarkGroup_toArray1(self._obj))

    def toArray(self, array):
        """
           @param array the array into which the elements of the list are to be stored, if it is big enough; otherwise, a
           new array of the same runtime type is allocated for this purpose.
           @return an array containing the product nodes, never <code>null</code>, but the array can have zero length
        """
        return T(PlacemarkGroup_toArray2(self._obj, array._obj))

    def indexOf(self, name):
        return PlacemarkGroup_indexOf2(self._obj, name)

    def indexOf(self, element):
        return PlacemarkGroup_indexOf1(self._obj, element._obj)

    def getByDisplayName(self, displayName):
        """
           @param displayName the display name
           @return the product node with the given display name.
        """
        return T(PlacemarkGroup_getByDisplayName(self._obj, displayName))

    def get(self, name):
        """
           @param name the name
           @return the product node with the given name.
        """
        return T(PlacemarkGroup_get2(self._obj, name))

    def contains(self, name):
        """
           Tests whether a node with the given name is contained in this group.
           @param name the name
           @return true, if so
        """
        return PlacemarkGroup_contains2(self._obj, name)

    def contains(self, node):
        """
           Tests whether the given product is contained in this list.
           @param node the node
           @return true, if so
        """
        return PlacemarkGroup_contains1(self._obj, node._obj)

    def add(self, node):
        """
           Adds the given node to this group.
           @param node the node to be added, ignored if <code>null</code>
           @return true, if the node has been added
        """
        return PlacemarkGroup_add3(self._obj, node._obj)

    def add(self, index, node):
        """
           Adds the given node to this group.
           @param index the index.
           @param node  the node to be added, ignored if <code>null</code>
        """
        PlacemarkGroup_add1(self._obj, index, node._obj)
        return

    def remove(self, node):
        """
           Removes the given node from this group.
           @param node the node to be removed
           @return true, if the node was removed
        """
        return PlacemarkGroup_remove1(self._obj, node._obj)

    def removeAll(self):
        """
           Removes all nodes from this group.
        """
        PlacemarkGroup_removeAll(self._obj)
        return

    def clearRemovedList(self):
        PlacemarkGroup_clearRemovedList(self._obj)
        return

    def getRemovedNodes(self):
        """
           Gets all removed node nodes.
           @return a collection of all removed node nodes.
        """
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
        """
           @return The owner node of this node.
        """
        return ProductNode(PlacemarkGroup_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return PlacemarkGroup_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        PlacemarkGroup_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return PlacemarkGroup_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        PlacemarkGroup_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return PlacemarkGroup_isModified(self._obj)

    def toString(self):
        return PlacemarkGroup_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return PlacemarkGroup_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(PlacemarkGroup_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(PlacemarkGroup_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(PlacemarkGroup_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return PlacemarkGroup_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return PlacemarkGroup_getProductRefString(self._obj)

    def getRawStorageSize(self):
        """
           Gets an estimated, raw storage size in bytes of this product node.
           @return the size in bytes.
        """
        return PlacemarkGroup_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        PlacemarkGroup_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        PlacemarkGroup_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        PlacemarkGroup_removeFromFile(self._obj, productWriter._obj)
        return


"""
<code>Product</code> instances are an in-memory representation of a remote sensing data product. The product is more
an abstract hull containing references to the data of the product or readers to retrieve the data on demant. The
product itself does not hold the remote sensing data. Data products can contain multiple geophysical parameters
stored as bands and can also have multiple metadata attributes. Also, a <code>Product</code> can contain any number
of <code>TiePointGrids</code> holding the tie point data.

Every product can also have a product reader and writer assigned to it. The reader represents the data source from
which a product was created, whereas the writer represents the data sink. Both, the source and the sink must not
necessarily store data in the same format. Furthermore, it is not mandatory for a product to have both of them.
"""
class Product:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newProduct(name, type, sceneRasterWidth, sceneRasterHeight):
        """
           Creates a new product without any reader (in-memory product)
           @param name              the product name
           @param type              the product type
           @param sceneRasterWidth  the scene width in pixels for this data product
           @param sceneRasterHeight the scene height in pixels for this data product
        """
        return Product(Product_newProduct(name, type, sceneRasterWidth, sceneRasterHeight))

    def getFileLocation(self):
        """
           Retrieves the disk location of this product. The return value can be <code>null</code> when the product has no
           disk location (pure virtual memory product)
           @return the file location, may be <code>null</code>
        """
        return File(Product_getFileLocation(self._obj))

    def setFileLocation(self, fileLocation):
        """
           Sets the file location for this product.
           @param fileLocation the file location, may be <code>null</code>
        """
        Product_setFileLocation(self._obj, fileLocation._obj)
        return

    def getProductType(self):
        """
           Gets the product type string.
           @return the product type string
        """
        return Product_getProductType(self._obj)

    def setProductType(self, productType):
        """
           Sets the product type of this product.
           @param productType the product type.
        """
        Product_setProductType(self._obj, productType)
        return

    def setProductReader(self, reader):
        """
           Sets the product reader which will be used to create this product in-memory represention from an external source
           and which will be used to (re-)load band rasters.
           @param reader the product reader.
           @throws IllegalArgumentException if the given reader is null.
        """
        Product_setProductReader(self._obj, reader._obj)
        return

    def getProductReader(self):
        """
           Returns the reader which was used to create this product in-memory represention from an external source and which
           will be used to (re-)load band rasters.
           @return the product reader, can be <code>null</code>
        """
        return ProductReader(Product_getProductReader(self._obj))

    def setProductWriter(self, writer):
        """
           Sets the writer which will be used to write modifications of this product's in-memory represention to an external
           destination.
           @param writer the product writer, can be <code>null</code>
        """
        Product_setProductWriter(self._obj, writer._obj)
        return

    def getProductWriter(self):
        """
           Returns the writer which will be used to write modifications of this product's in-memory represention to an
           external destination.
           @return the product writer, can be <code>null</code>
        """
        return ProductWriter(Product_getProductWriter(self._obj))

    def writeHeader(self, output):
        """
           Writes the header of a data product.
           @param output an object representing a valid output for this writer, might be a <code>ImageOutputStream</code>
           or a <code>File</code> or other <code>Object</code> to use for future decoding.
           @throws IllegalArgumentException if <code>output</code> is <code>null</code> or it's type is none of the
           supported output types.
           @throws IOException              if an I/O error occurs
        """
        Product_writeHeader(self._obj, output._obj)
        return

    def closeProductReader(self):
        """
           Closes and clears this product's reader (if any).
           @throws IOException if an I/O error occurs
           @see #closeIO
        """
        Product_closeProductReader(self._obj)
        return

    def closeProductWriter(self):
        """
           Closes and clears this product's writer (if any).
           @throws IOException if an I/O error occurs
           @see #closeIO
        """
        Product_closeProductWriter(self._obj)
        return

    def closeIO(self):
        """
           Closes the file I/O for this product. Calls in sequence <code>{@link #closeProductReader}</code>  and
           <code>{@link #closeProductWriter}</code>. The <code>{@link #dispose}</code> method is <b>not</b> called, but
           should be called if the product instance is no longer in use.
           @throws IOException if an I/O error occurs
           @see #closeProductReader
           @see #closeProductWriter
           @see #dispose
        """
        Product_closeIO(self._obj)
        return

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
           
           This implementation also calls the <code>closeIO</code> in order to release all open I/O resources.
        """
        Product_dispose(self._obj)
        return

    def getPointingFactory(self):
        """
           Gets the pointing factory associated with this data product.
           @return the pointing factory or null, if none
        """
        return PointingFactory(Product_getPointingFactory(self._obj))

    def setPointingFactory(self, pointingFactory):
        """
           Sets the pointing factory for this data product.
           @param pointingFactory the pointing factory
        """
        Product_setPointingFactory(self._obj, pointingFactory._obj)
        return

    def setGeoCoding(self, geoCoding):
        """
           Geo-codes this data product.
           @param geoCoding the geo-coding, if <code>null</code> geo-coding is removed
           @throws IllegalArgumentException <br>- if the given <code>GeoCoding</code> is a <code>TiePointGeoCoding</code>
           and <code>latGrid</code> or <code>lonGrid</code> are not instances of tie point
           grids in this product. <br>- if the given <code>GeoCoding</code> is a
           <code>MapGeoCoding</code> and its <code>MapInfo</code> is <code>null</code>
           <br>- if the given <code>GeoCoding</code> is a <code>MapGeoCoding</code> and the
           <code>sceneWith</code> or <code>sceneHeight</code> of its <code>MapInfo</code>
           is not equal to this products <code>sceneRasterWidth</code> or
           <code>sceneRasterHeight</code>
        """
        Product_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getGeoCoding(self):
        """
           Returns the geo-coding used for this data product.
           @return the geo-coding, can be <code>null</code> if this product is not geo-coded.
        """
        return GeoCoding(Product_getGeoCoding(self._obj))

    def isUsingSingleGeoCoding(self):
        """
           Tests if all bands of this product are using a single, uniform geo-coding. Uniformity is tested by comparing
           the band's geo-coding against the geo-coding of this product using the {@link Object#equals(Object)} method.
           If this product does not have a geo-coding, the method returns false.
           @return true, if so
        """
        return Product_isUsingSingleGeoCoding(self._obj)

    def transferGeoCodingTo(self, destProduct, subsetDef):
        """
           Transfers the geo-coding of this product instance to the {@link Product destProduct} with respect to
           the given {@link ProductSubsetDef subsetDef}.
           @param destProduct the destination product
           @param subsetDef   the definition of the subset, may be <code>null</code>
           @return true, if the geo-coding could be transferred.
        """
        return Product_transferGeoCodingTo(self._obj, destProduct._obj, subsetDef._obj)

    def getSceneRasterWidth(self):
        """
           Returns the scene width in pixels for this data product.
           @return the scene width in pixels for this data product.
        """
        return Product_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        """
           Returns the scene height in pixels for this data product.
           @return the scene height in pixels for this data product.
        """
        return Product_getSceneRasterHeight(self._obj)

    def getStartTime(self):
        """
           Gets the (sensing) start time associated with the first raster data line.
           
           For Level-1/2 products this is
           the data-take time associated with the first raster data line.
           For Level-3 products, this could be the start time of first input product
           contributing data.
           @return the sensing start time, can be null e.g. for non-swath products
        """
        return ProductData_UTC(Product_getStartTime(self._obj))

    def setStartTime(self, startTime):
        """
           Sets the (sensing) start time of this product.
           
           For Level-1/2 products this is
           the data-take time associated with the first raster data line.
           For Level-3 products, this could be the start time of first input product
           contributing data.
           @param startTime the sensing start time, can be null
        """
        Product_setStartTime(self._obj, startTime._obj)
        return

    def getEndTime(self):
        """
           Gets the (sensing) stop time associated with the last raster data line.
           
           For Level-1/2 products this is
           the data-take time associated with the last raster data line.
           For Level-3 products, this could be the end time of last input product
           contributing data.
           @return the stop time , can be null e.g. for non-swath products
        """
        return ProductData_UTC(Product_getEndTime(self._obj))

    def setEndTime(self, endTime):
        """
           Sets the (sensing) stop time associated with the first raster data line.
           
           For Level-1/2 products this is
           the data-take time associated with the last raster data line.
           For Level-3 products, this could be the end time of last input product
           contributing data.
           @param endTime the sensing stop time, can be null
        """
        Product_setEndTime(self._obj, endTime._obj)
        return

    def getMetadataRoot(self):
        """
           Gets the root element of the associated metadata.
           @return the metadata root element
        """
        return MetadataElement(Product_getMetadataRoot(self._obj))

    def getBandGroup(self):
        """
           Gets the band group of this product.
           @return The group of all bands.
        """
        return ProductNodeGroup(Product_getBandGroup(self._obj))

    def getTiePointGridGroup(self):
        """
           Gets the tie-point grid group of this product.
           @return The group of all tie-point grids.
        """
        return ProductNodeGroup(Product_getTiePointGridGroup(self._obj))

    def addTiePointGrid(self, tiePointGrid):
        """
           Adds the given tie-point grid to this product.
           @param tiePointGrid the tie-point grid to added, ignored if <code>null</code>
        """
        Product_addTiePointGrid(self._obj, tiePointGrid._obj)
        return

    def removeTiePointGrid(self, tiePointGrid):
        """
           Removes the tie-point grid from this product.
           @param tiePointGrid the tie-point grid to be removed, ignored if <code>null</code>
           @return <code>true</code> if node could be removed
        """
        return Product_removeTiePointGrid(self._obj, tiePointGrid._obj)

    def getNumTiePointGrids(self):
        """
           Returns the number of tie-point grids contained in this product
           @return the number of tie-point grids
        """
        return Product_getNumTiePointGrids(self._obj)

    def getTiePointGridAt(self, index):
        """
           Returns the tie-point grid at the given index.
           @param index the tie-point grid index
           @return the tie-point grid at the given index
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return TiePointGrid(Product_getTiePointGridAt(self._obj, index))

    def getTiePointGridNames(self):
        """
           Returns a string array containing the names of the tie-point grids contained in this product
           @return a string array containing the names of the tie-point grids contained in this product. If this product has
           no tie-point grids a zero-length-array is returned.
        """
        return Product_getTiePointGridNames(self._obj)

    def getTiePointGrids(self):
        """
           Returns an array of tie-point grids contained in this product
           @return an array of tie-point grids contained in this product. If this product has no  tie-point grids a
           zero-length-array is returned.
        """
        return TiePointGrid(Product_getTiePointGrids(self._obj))

    def getTiePointGrid(self, name):
        """
           Returns the tie-point grid with the given name.
           @param name the tie-point grid name
           @return the tie-point grid with the given name or <code>null</code> if a tie-point grid with the given name is
           not contained in this product.
        """
        return TiePointGrid(Product_getTiePointGrid(self._obj, name))

    def containsTiePointGrid(self, name):
        """
           Tests if a tie-point grid with the given name is contained in this product.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a tie-point grid with the given name is contained in this product,
           <code>false</code> otherwise
        """
        return Product_containsTiePointGrid(self._obj, name)

    def addBand(self, band):
        """
           Adds the given band to this product.
           @param band the band to added, must not be <code>null</code>
        """
        Product_addBand(self._obj, band._obj)
        return

    def addBand(self, bandName, dataType):
        """
           Creates a new band with the given name and data type and adds it to this product and returns it.
           @param bandName the new band's name
           @param dataType the raster data type, must be one of the multiple <code>ProductData.TYPE_<i>X</i></code>
           constants
           @return the new band which has just been added
        """
        return Band(Product_addNewBand(self._obj, bandName, dataType))

    def addBand(self, bandName, expression):
        """
           Creates a new band with the given name and adds it to this product and returns it.
           The new band's data type is {@code float} and it's samples are computed from the given band maths expression.
           @param bandName   the new band's name
           @param expression the band maths expression
           @return the new band which has just been added
        """
        return Band(Product_addComputedBand(self._obj, bandName, expression))

    def removeBand(self, band):
        """
           Removes the given band from this product.
           @param band the band to be removed, ignored if <code>null</code>
           @return {@code true} if removed succesfully, otherwise {@code false}
        """
        return Product_removeBand(self._obj, band._obj)

    def getNumBands(self):
        """
           @return the number of bands contained in this product.
        """
        return Product_getNumBands(self._obj)

    def getBandAt(self, index):
        """
           Returns the band at the given index.
           @param index the band index
           @return the band at the given index
           @throws IndexOutOfBoundsException if the index is out of bounds
        """
        return Band(Product_getBandAt(self._obj, index))

    def getBandNames(self):
        """
           Returns a string array containing the names of the bands contained in this product
           @return a string array containing the names of the bands contained in this product. If this product has no bands
           a zero-length-array is returned.
        """
        return Product_getBandNames(self._obj)

    def getBands(self):
        """
           Returns an array of bands contained in this product
           @return an array of bands contained in this product. If this product has no bands a zero-length-array is
           returned.
        """
        return Band(Product_getBands(self._obj))

    def getBand(self, name):
        """
           Returns the band with the given name.
           @param name the band name
           @return the band with the given name or <code>null</code> if a band with the given name is not contained in this
           product.
           @throws IllegalArgumentException if the given name is <code>null</code> or empty.
        """
        return Band(Product_getBand(self._obj, name))

    def getBandIndex(self, name):
        """
           Returns the index for the band with the given name.
           @param name the band name
           @return the band index or <code>-1</code> if a band with the given name is not contained in this product.
           @throws IllegalArgumentException if the given name is <code>null</code> or empty.
        """
        return Product_getBandIndex(self._obj, name)

    def containsBand(self, name):
        """
           Tests if a band with the given name is contained in this product.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a band with the given name is contained in this product, <code>false</code>
           otherwise
           @throws IllegalArgumentException if the given name is <code>null</code> or empty.
        """
        return Product_containsBand(self._obj, name)

    def containsRasterDataNode(self, name):
        """
           Tests if a raster data node with the given name is contained in this product. Raster data nodes can be bands or
           tie-point grids.
           @param name the name, must not be <code>null</code>
           @return <code>true</code> if a raster data node with the given name is contained in this product,
           <code>false</code> otherwise
        """
        return Product_containsRasterDataNode(self._obj, name)

    def getRasterDataNode(self, name):
        """
           Gets the raster data node with the given name. The method first searches for bands with the given name, then for
           tie-point grids. If neither bands nor tie-point grids exist with the given name, <code>null</code> is returned.
           @param name the name, must not be <code>null</code>
           @return the raster data node with the given name or <code>null</code> if a raster data node with the given name
           is not contained in this product.
        """
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
        """
           Tests if the given pixel position is within the product pixel bounds.
           @param x the x coordinate of the pixel position
           @param y the y coordinate of the pixel position
           @return true, if so
           @see #containsPixel(PixelPos)
        """
        return Product_containsPixel(self._obj, x, y)

    def getGcpGroup(self):
        """
           Gets the group of ground-control points (GCPs).
           Note that this method will create the group, if none exists already.
           @return the GCP group.
        """
        return PlacemarkGroup(Product_getGcpGroup(self._obj))

    def getPinGroup(self):
        """
           Gets the group of pins.
           Note that this method will create the group, if none exists already.
           @return the pin group.
        """
        return PlacemarkGroup(Product_getPinGroup(self._obj))

    def isCompatibleProduct(self, product, eps):
        """
           Checks whether or not the given product is compatible with this product.
           @param product the product to compare with
           @param eps     the maximum lat/lon error in degree
           @return <code>false</code> if the scene dimensions or geocoding are different, <code>true</code> otherwise.
        """
        return Product_isCompatibleProduct(self._obj, product._obj, eps)

    def parseExpression(self, expression):
        """
           Parses a mathematical expression given as a text string.
           @param expression a expression given as a text string, e.g. "radiance_4 / (1.0 + radiance_11)".
           @return a term parsed from the given expression string
           @throws ParseException if the expression could not successfully be parsed
        """
        return Term(Product_parseExpression(self._obj, expression))

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method subsequentially visits (calls <code>acceptVisitor</code> for) all bands, tie-point grids and flag
           codings. Finally it visits product metadata root element and calls <code>visitor.visit(this)</code>.
           @param visitor the visitor, must not be <code>null</code>
        """
        Product_acceptVisitor(self._obj, visitor._obj)
        return

    def addProductNodeListener(self, listener):
        """
           Adds a <code>ProductNodeListener</code> to this product. The <code>ProductNodeListener</code> is informed each
           time a node in this product changes.
           @param listener the listener to be added
           @return boolean if listener was added or not
        """
        return Product_addProductNodeListener(self._obj, listener._obj)

    def removeProductNodeListener(self, listener):
        """
           Removes a <code>ProductNodeListener</code> from this product.
           @param listener the listener to be removed.
        """
        Product_removeProductNodeListener(self._obj, listener._obj)
        return

    def getProductNodeListeners(self):
        return ProductNodeListener(Product_getProductNodeListeners(self._obj))

    def getRefNo(self):
        """
           @return The reference number of this product.
        """
        return Product_getRefNo(self._obj)

    def setRefNo(self, refNo):
        """
           Sets the reference number.
           @param refNo the reference number to set must be in the range 1 .. Integer.MAX_VALUE
           @throws IllegalArgumentException if the refNo is out of range
           @throws IllegalStateException
        """
        Product_setRefNo(self._obj, refNo)
        return

    def resetRefNo(self):
        Product_resetRefNo(self._obj)
        return

    def getProductManager(self):
        """
           Returns the product manager for this product.
           @return this product's manager, can be <code>null</code>
        """
        return ProductManager(Product_getProductManager(self._obj))

    def createBandArithmeticParser(self):
        """
           Creates a parser for band arithmetic expressions.
           The parser created will use a namespace comprising all tie-point grids, bands and flags of this product.
           @return a parser for band arithmetic expressions for this product, never null
        """
        return Parser(Product_createBandArithmeticParser(self._obj))

    def createBandArithmeticDefaultNamespace(self):
        """
           Creates a namespace to be used by parsers for band arithmetic expressions.
           The namespace created comprises all tie-point grids, bands and flags of this product.
           @return a namespace, never null
        """
        return WritableNamespace(Product_createBandArithmeticDefaultNamespace(self._obj))

    def createSubset(self, subsetDef, name, desc):
        """
           Creates a subset of this product. The returned product represents a true spatial and spectral subset of this
           product, but it has not loaded any bands into memory. If name or desc are null or empty, the name and the
           description from this product was used.
           @param subsetDef the product subset definition
           @param name      the name for the new product
           @param desc      the description for the new product
           @return the product subset, or <code>null</code> if the product/subset combination is not valid
           @throws IOException if an I/O error occurs
        """
        return Product(Product_createSubset(self._obj, subsetDef._obj, name, desc))

    def createProjectedProduct(self, mapInfo, name, desc):
        """
           Creates a map-projected version of this product.
           @param mapInfo the map information
           @param name    the name for the new product
           @param desc    the description for the new product
           @return the product subset, or <code>null</code> if the product/subset combination is not valid
           @throws IOException if an I/O error occurs
        """
        return Product(Product_createProjectedProduct(self._obj, mapInfo._obj, name, desc))

    def createFlippedProduct(self, flipType, name, desc):
        """
           Creates flipped raster-data version of this product.
           @param flipType the flip type, see <code>{@link ProductFlipper}</code>
           @param name     the name for the new product
           @param desc     the description for the new product
           @return the product subset, or <code>null</code> if the product/subset combination is not valid
           @throws IOException if an I/O error occurs
        """
        return Product(Product_createFlippedProduct(self._obj, flipType, name, desc))

    def setModified(self, modified):
        Product_setModified(self._obj, modified)
        return

    def getQuicklookBandName(self):
        """
           Gets the name of the band suitable for quicklook generation.
           @return the name of the quicklook band, or null if none has been defined
        """
        return Product_getQuicklookBandName(self._obj)

    def setQuicklookBandName(self, quicklookBandName):
        """
           Sets the name of the band suitable for quicklook generation.
           @param quicklookBandName the name of the quicklook band, or null
        """
        Product_setQuicklookBandName(self._obj, quicklookBandName)
        return

    def createPixelInfoString(self, pixelX, pixelY):
        """
           Creates a string containing all available information at the given pixel position. The string returned is a line
           separated text with each line containing a key/value pair.
           @param pixelX the pixel X co-ordinate
           @param pixelY the pixel Y co-ordinate
           @return the info string at the given position
        """
        return Product_createPixelInfoString(self._obj, pixelX, pixelY)

    def getRemovedChildNodes(self):
        """
           @return All removed child nodes. Array may be empty.
        """
        return ProductNode(Product_getRemovedChildNodes(self._obj))

    def canBeOrthorectified(self):
        """
           Checks whether or not this product can be ortorectified.
           @return true if {@link Band#canBeOrthorectified()} returns true for all bands, false otherwise
        """
        return Product_canBeOrthorectified(self._obj)

    def getPreferredTileSize(self):
        """
           Gets the preferred tile size which may be used for a the {@link java.awt.image.RenderedImage rendered image}
           created for a {@link RasterDataNode} of this product.
           @return the preferred tile size, may be <code>null</null> if not specified
           @see RasterDataNode#getSourceImage()
           @see RasterDataNode# setSourceImage (java.awt.image.RenderedImage)
        """
        return Dimension(Product_getPreferredTileSize(self._obj))

    def setPreferredTileSize(self, tileWidth, tileHeight):
        """
           Sets the preferred tile size which may be used for a the {@link java.awt.image.RenderedImage rendered image}
           created for a {@link RasterDataNode} of this product.
           @param tileWidth  the preferred tile width
           @param tileHeight the preferred tile height
           @see #setPreferredTileSize(java.awt.Dimension)
        """
        Product_setPreferredTileSize(self._obj, tileWidth, tileHeight)
        return

    def getAllFlagNames(self):
        """
           Returns the names of all flags of all flag datasets contained this product.
           
           A flag name contains the dataset (a band of this product) and the actual flag name as defined in the
           flag-coding associated with the dataset. The general format for the flag name strings returned is therefore
           <code>"<i>dataset</i>.<i>flag_name</i>"</code>.
           
           The method is used to find out which flags a product has in order to use them in bit-mask expressions.
           @return the array of all flag names. If this product does not support flags, an empty array is returned, but
           never <code>null</code>.
           @see #parseExpression(String)
        """
        return Product_getAllFlagNames(self._obj)

    def getAutoGrouping(self):
        """
           Gets the auto-grouping applicable to product nodes contained in this product.
           @return The auto-grouping or {@code null}.
        """
        return Product_AutoGrouping(Product_getAutoGrouping(self._obj))

    def setAutoGrouping(self, pattern):
        """
           Sets the auto-grouping applicable to product nodes contained in this product.
           A given {@code pattern} parameter is a textual representation of the auto-grouping.
           The syntax for the pattern is:
           <pre>
           pattern    :=  &lt;groupPath&gt; {':' &lt;groupPath&gt;} | "" (empty string)
           groupPath  :=  &lt;groupName&gt; {'/' &lt;groupName&gt;}
           groupName  :=  any non-empty string without characters ':' and '/'
           </pre>
           An example for {@code pattern} applicable to Envisat AATSR data is
           <pre>
           nadir/reflec:nadir/btemp:fward/reflec:fward/btemp:nadir:fward
           </pre>
           @param pattern The auto-grouping pattern.
        """
        Product_setAutoGrouping(self._obj, pattern)
        return

    def addMask(self, maskName, expression, description, color, transparency):
        """
           Creates a new mask using a band arithmetic expression
           and adds it to this product and returns it.
           @param maskName     the new mask's name
           @param expression   the band arithmetic expression
           @param description  the mask's description
           @param color        the display color
           @param transparency the display transparency
           @return the new mask which has just been added
        """
        return Mask(Product_addComputedMask(self._obj, maskName, expression, description, color._obj, transparency))

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(Product_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return Product_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        Product_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return Product_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        Product_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return Product_isModified(self._obj)

    def toString(self):
        return Product_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return Product_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(Product_getProduct(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return Product_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return Product_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        Product_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        Product_removeFromFile(self._obj, productWriter._obj)
        return


class Point2D:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>ProductNode</code> is the base class for all nodes within a remote sensing data product and even the data
product itself.
"""
class T:
    def __init__(self, obj):
        self._obj = obj


"""
A visitor for a product and all other product nodes. This interface is part of the <i>visitor pattern</i> used to
visit all nodes of a data product. Implementations of this interface can be passed to the <code>acceptVisitor</code>
method of an <code>Product</code> (or any other <code>ProductNode</code>).
@see Product#acceptVisitor(ProductVisitor)
@see ProductNode#acceptVisitor(ProductVisitor)
"""
class ProductVisitor:
    def __init__(self, obj):
        self._obj = obj


"""
The scaling method used for geophysical value transformation in a {@link Band}.
"""
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


"""
A listener for the product manager.
"""
class ProductManager_Listener:
    def __init__(self, obj):
        self._obj = obj


class GeoTIFFMetadata:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>ColorPaletteDef</code> class represents a curve that is used to transform the sample values of a
geo-physical band into color palette indexes.

 This special implemnentation of a gradation curve also provides separate color values for each of the tie points
contained in the curve. This allows a better image interpretation because certain colors correspond to certain sample
values even if the curve points are used to create color gradient palettes.
"""
class ColorPaletteDef:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newColorPaletteDef(minSample, maxSample):
        return ColorPaletteDef(ColorPaletteDef_newColorPaletteDefFromRange(minSample, maxSample))

    @staticmethod
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
        """
           creates a new point between the point at the given index
           @param index   the index
           @param scaling the scaling
           @return true, if a point has been inserted
        """
        return ColorPaletteDef_createPointAfter(self._obj, index, scaling._obj)

    @staticmethod
    def getCenterColor(c1, c2):
        """
           Creates the center color between the given two colors.
           @param c1 1st color
           @param c2 2nd color
           @return the center color
        """
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

    @staticmethod
    def loadColorPaletteDef(file):
        """
           Loads a color palette definition from the given file
           @param file the file
           @return the color palette definition, never null
           @throws IOException if an I/O error occurs
        """
        return ColorPaletteDef(ColorPaletteDef_loadColorPaletteDef(file._obj))

    @staticmethod
    def storeColorPaletteDef(colorPaletteDef, file):
        """
           Stores this color palette definition in the given file
           @param colorPaletteDef thje color palette definition
           @param file            the file
           @throws IOException if an I/O error occurs
        """
        ColorPaletteDef_storeColorPaletteDef(colorPaletteDef._obj, file._obj)
        return

    def dispose(self):
        """
           Releases all of the resources used by this color palette definition and all of its owned children. Its primary
           use is to allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
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


"""
This class contains information about how a product's raster data node is displayed as an image.
"""
class ImageInfo:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newImageInfo(colorPaletteDef):
        """
           Constructs a new image information instance.
           @param colorPaletteDef the color palette definition
        """
        return ImageInfo(ImageInfo_newImageInfoPalette(colorPaletteDef._obj))

    @staticmethod
    def newImageInfo(rgbChannelDef):
        """
           Constructs a new RGB image information instance.
           @param rgbChannelDef the RGB channel definition
        """
        return ImageInfo(ImageInfo_newImageInfoRGB(rgbChannelDef._obj))

    def getColorPaletteDef(self):
        """
           Gets the color palette definition as used for images created from single bands.
           @return The color palette definition. Can be {@code null}.
           In this case {@link #getRgbChannelDef()} is non-null.
        """
        return ColorPaletteDef(ImageInfo_getColorPaletteDef(self._obj))

    def getRgbChannelDef(self):
        """
           Gets the RGB(A) channel definition as used for images created from 3 tp 4 bands.
           @return The RGB(A) channel definition.
           Can be {@code null}. In this case {@link #getColorPaletteDef()} is non-null.
        """
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
        """
           Gets the number of color components the image shall have using an instance of this {@code ImageInfo}.
           @return {@code 3} for RGB images, {@code 4} for RGB images with an alpha channel (transparency)
        """
        return ImageInfo_getColorComponentCount(self._obj)

    def createIndexColorModel(self, scaling):
        return IndexColorModel(ImageInfo_createIndexColorModel(self._obj, scaling._obj))

    def createComponentColorModel(self):
        return ComponentColorModel(ImageInfo_createComponentColorModel(self._obj))

    def clone(self):
        """
           Creates and returns a copy of this object.
           @return a copy of this object
        """
        return Object(ImageInfo_clone(self._obj))

    def createDeepCopy(self):
        """
           Creates and returns a "deep" copy of this object. The method simply returns the value of
           {@link #clone()}.
           @return a copy of this object
        """
        return ImageInfo(ImageInfo_createDeepCopy(self._obj))

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        ImageInfo_dispose(self._obj)
        return

    def setColors(self, colors):
        """
           Sets the colours of the colour palette of this image info.
           @param colors the new colours
        """
        ImageInfo_setColors(self._obj, colors._obj)
        return

    def setColorPaletteDef(self, colorPaletteDef, minSample, maxSample, autoDistribute):
        """
           Transfers the colour palette into this image info.
           @param colorPaletteDef another colour palette
           @param minSample       the minium allowed sample value in the new colour palette
           @param maxSample       the maximum allowed sample value in the new colour palette
           @param autoDistribute  if true, points are distributed between minSample/maxSample.
        """
        ImageInfo_setColorPaletteDef(self._obj, colorPaletteDef._obj, minSample, maxSample, autoDistribute)
        return

    @staticmethod
    def getHistogramMatching(mode):
        """
           Converts a string to a histogram matching.
           @param mode the histogram matching string
           @return the histogram matching. {@link ImageInfo.HistogramMatching#None} if {@code maode} is not "Equalize" or "Normalize".
        """
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

    @staticmethod
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


"""
A tie-point grid contains the data for geophysical parameter in remote sensing data products. Tie-point grid are
two-dimensional images which hold their pixel values (samples) in a <code>float</code> array. 

Usually, tie-point grids are a sub-sampling of a data product's scene resolution.
"""
class TiePointGrid:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints):
        """
           Constructs a new <code>TiePointGrid</code> with the given tie point grid properties.
           @param name         the name of the new object
           @param gridWidth    the width of the tie-point grid in pixels
           @param gridHeight   the height of the tie-point grid in pixels
           @param offsetX      the X co-ordinate of the first (upper-left) tie-point in pixels
           @param offsetY      the Y co-ordinate of the first (upper-left) tie-point in pixels
           @param subSamplingX the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param subSamplingY the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param tiePoints    the tie-point data values, must be an array of the size <code>gridWidth * gridHeight</code>
        """
        return TiePointGrid(TiePointGrid_newTiePointGrid1(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints))

    @staticmethod
    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity):
        """
           Constructs a new <code>TiePointGrid</code> with the given tie point grid properties.
           @param name          the name of the new object
           @param gridWidth     the width of the tie-point grid in pixels
           @param gridHeight    the height of the tie-point grid in pixels
           @param offsetX       the X co-ordinate of the first (upper-left) tie-point in pixels
           @param offsetY       the Y co-ordinate of the first (upper-left) tie-point in pixels
           @param subSamplingX  the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param subSamplingY  the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param tiePoints     the tie-point data values, must be an array of the size <code>gridWidth * gridHeight</code>
           @param discontinuity the discontinuity mode, can be either {@link #DISCONT_NONE} or {@link #DISCONT_AT_180}
           {@link #DISCONT_AT_360}
        """
        return TiePointGrid(TiePointGrid_newTiePointGrid2(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, discontinuity))

    @staticmethod
    def newTiePointGrid(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles):
        """
           Constructs a new <code>TiePointGrid</code> with the given tie point grid properties.
           @param name           the name of the new object
           @param gridWidth      the width of the tie-point grid in pixels
           @param gridHeight     the height of the tie-point grid in pixels
           @param offsetX        the X co-ordinate of the first (upper-left) tie-point in pixels
           @param offsetY        the Y co-ordinate of the first (upper-left) tie-point in pixels
           @param subSamplingX   the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param subSamplingY   the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to. Must not be less than one.
           @param tiePoints      the tie-point data values, must be an array of the size <code>gridWidth * gridHeight</code>
           @param containsAngles if true, the {@link #getDiscontinuity() angular discontinuity} is derived from the provided tie-point data values
        """
        return TiePointGrid(TiePointGrid_newTiePointGrid3(name, gridWidth, gridHeight, offsetX, offsetY, subSamplingX, subSamplingY, tiePoints, containsAngles))

    @staticmethod
    def getDiscontinuity(tiePoints):
        """
           Determines the angular discontinuity of the given tie point values.
           @return the angular discontinuity, will always be either {@link #DISCONT_AT_180} or
           {@link #DISCONT_AT_360}
        """
        return TiePointGrid_getDiscontinuity2(tiePoints)

    def getDiscontinuity(self):
        """
           Gets the angular discontinuity.
           @return the angular discontinuity, will always be either {@link #DISCONT_NONE} or {@link #DISCONT_AT_180} or
           {@link #DISCONT_AT_360}
        """
        return TiePointGrid_getDiscontinuity1(self._obj)

    def setDiscontinuity(self, discontinuity):
        """
           Sets the angular discontinuity.
           @param discontinuity angular discontinuity, can be either {@link #DISCONT_NONE} or {@link #DISCONT_AT_180} or
           {@link #DISCONT_AT_360}
        """
        TiePointGrid_setDiscontinuity(self._obj, discontinuity)
        return

    def isFloatingPointType(self):
        """
           Returns <code>true</code>
           @return true
        """
        return TiePointGrid_isFloatingPointType(self._obj)

    def getGeophysicalDataType(self):
        """
           Returns the geophysical data type of this <code>RasterDataNode</code>. The value retuned is always one of the
           <code>ProductData.TYPE_XXX</code> constants.
           @return the geophysical data type
           @see ProductData
        """
        return TiePointGrid_getGeophysicalDataType(self._obj)

    def getSceneRasterData(self):
        """
           Gets a raster data holding this tie-point's interpolated pixel data for an entire product scene. 
           
           In opposite to the <code>getRasterData</code> method, this method returns raster data that has at least
           <code>getBandOutputRasterWidth()*getBandOutputRasterHeight()</code> elements of the given data type to store
           the scene's pixels.
           @return raster data covering the pixels for a complete scene
           @see #getRasterData
           @see #getRasterWidth
           @see #getRasterHeight
           @see #getSceneRasterWidth
           @see #getSceneRasterHeight
        """
        return ProductData(TiePointGrid_getSceneRasterData(self._obj))

    def getSceneRasterWidth(self):
        """
           Returns the width in pixels of the scene represented by this tie-point grid. The value returned is
           <code>(getRasterWidth() - 1) * getSubSamplingX() + 1</code>
           @return the scene width in pixels
        """
        return TiePointGrid_getSceneRasterWidth(self._obj)

    def getSceneRasterHeight(self):
        """
           Returns the height in pixels of the scene represented by this tie-point grid. The value returned is
           <code>(getRasterHeight() - 1) * getSubSamplingY() + 1</code>
           @return the scene height in pixels
        """
        return TiePointGrid_getSceneRasterHeight(self._obj)

    def getOffsetX(self):
        """
           Retrieves the x co-ordinate of the first (upper-left) tie-point in pixels.
        """
        return TiePointGrid_getOffsetX(self._obj)

    def getOffsetY(self):
        """
           Retrieves the y co-ordinate of the first (upper-left) tie-point in pixels.
        """
        return TiePointGrid_getOffsetY(self._obj)

    def getSubSamplingX(self):
        """
           Returns the sub-sampling in X-direction given in the pixel co-ordinates of the data product to which this
           tie-pint grid belongs to.
           @return the sub-sampling in X-direction, never less than one.
        """
        return TiePointGrid_getSubSamplingX(self._obj)

    def getSubSamplingY(self):
        """
           Returns the sub-sampling in Y-direction given in the pixel co-ordinates of the data product to which this
           tie-pint grid belongs to.
           @return the sub-sampling in Y-direction, never less than one.
        """
        return TiePointGrid_getSubSamplingY(self._obj)

    def getTiePoints(self):
        """
           Gets the data array holding this band's pixel samples.
           @return the data array for this band, or <code>null</code> if no data has been loaded
           @see ProductData#getElems
        """
        return TiePointGrid_getTiePoints(self._obj)

    def getPixelInt(self, x, y):
        """
           Gets the interpolated sample for the pixel located at (x,y) as an integer value. 
           
           If the pixel co-odinates given by (x,y) are not covered by this tie-point grid, the method extrapolates.
           @param x The X co-ordinate of the pixel location
           @param y The Y co-ordinate of the pixel location
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
        """
        return TiePointGrid_getPixelInt(self._obj, x, y)

    def dispose(self):
        TiePointGrid_dispose(self._obj)
        return

    def getPixelFloat(self, x, y):
        """
           Computes the interpolated sample for the pixel located at (x,y). 
           
           If the pixel co-odinates given by (x,y) are not covered by this tie-point grid, the method extrapolates.
           @param x The X co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @param y The Y co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
        """
        return TiePointGrid_getPixelFloat2(self._obj, x, y)

    def getPixelFloat(self, x, y):
        """
           Computes the interpolated sample for the pixel located at (x,y) given as floating point co-ordinates. 
           
           If the pixel co-odinates given by (x,y) are not covered by this tie-point grid, the method extrapolates.
           @param x The X co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @param y The Y co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
        """
        return TiePointGrid_getPixelFloat1(self._obj, x, y)

    def getPixelDouble(self, x, y):
        """
           Gets the interpolated sample for the pixel located at (x,y) as a double value. 
           
           If the pixel co-ordinates given by (x,y) are not covered by this tie-point grid, the method extrapolates.
           @param x The X co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @param y The Y co-ordinate of the pixel location, given in the pixel co-ordinates of the data product to which
           this tie-pint grid belongs to.
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
        """
        return TiePointGrid_getPixelDouble(self._obj, x, y)

    def setPixelInt(self, x, y, pixelValue):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixelInt(self._obj, x, y, pixelValue)
        return

    def setPixelFloat(self, x, y, pixelValue):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixelFloat(self._obj, x, y, pixelValue)
        return

    def setPixelDouble(self, x, y, pixelValue):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixelDouble(self._obj, x, y, pixelValue)
        return

    def getPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product with and height as integer array. If the given
           array is <code>null</code> a new one was created and returned.
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the integer array to be filled with data
           @param pm     a monitor to inform the user about progress
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_getPixels6(self._obj, x, y, w, h, pixels, pm._obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product width and height as float array. If the given
           array is <code>null</code> a new one is created and returned.
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the float array to be filled with data
           @param pm     a monitor to inform the user about progress
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_getPixels4(self._obj, x, y, w, h, pixels, pm._obj)

    def getPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product with and height as double array. If the given
           array is <code>null</code> a new one was created and returned.
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the double array to be filled with data
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_getPixels2(self._obj, x, y, w, h, pixels, pm._obj)

    def setPixels(self, x, y, w, h, pixels):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixels3(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixels2(self._obj, x, y, w, h, pixels)
        return

    def setPixels(self, x, y, w, h, pixels):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_setPixels1(self._obj, x, y, w, h, pixels)
        return

    def readPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product with and height as float array. If the given
           array is <code>null</code> a new one was created and returned.
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the integer array to be filled with data
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_readPixels6(self._obj, x, y, w, h, pixels, pm._obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product with and height as float array. If the given
           array is <code>null</code> a new one was created and returned. *
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the float array to be filled with data
           @param pm     a monitor to inform the user about progress
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_readPixels4(self._obj, x, y, w, h, pixels, pm._obj)

    def readPixels(self, x, y, w, h, pixels, pm):
        """
           Retrieves an array of tie point data interpolated to the product with and height as double array. If the given
           array is <code>null</code> a new one was created and returned.
           @param x      the x coordinate of the array to be read
           @param y      the y coordinate of the array to be read
           @param w      the width of the array to be read
           @param h      the height of the array to be read
           @param pixels the double array to be filled with data
           @param pm     a monitor to inform the user about progress
           @throws IllegalArgumentException if the length of the given array is less than <code>w*h</code>.
        """
        return TiePointGrid_readPixels2(self._obj, x, y, w, h, pixels, pm._obj)

    def writePixels(self, x, y, w, h, pixels, pm):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_writePixels6(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_writePixels4(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def writePixels(self, x, y, w, h, pixels, pm):
        """
           This method is not implemented because pixels are read-only in tie-point grids.
        """
        TiePointGrid_writePixels2(self._obj, x, y, w, h, pixels, pm._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        """
           Reads raster data from this dataset into the user-supplied raster data buffer. 
           
           This method always directly (re-)reads this band's data from its associated data source into the given data
           buffer.
           @param offsetX    the X-offset in the raster co-ordinates where reading starts
           @param offsetY    the Y-offset in the raster co-ordinates where reading starts
           @param width      the width of the raster data buffer
           @param height     the height of the raster data buffer
           @param rasterData a raster data buffer receiving the pixels to be read
           @param pm         a monitor to inform the user about progress
           @throws java.io.IOException      if an I/O error occurs
           @throws IllegalArgumentException if the raster is null
           @throws IllegalStateException    if this product raster was not added to a product so far, or if the product to
           which this product raster belongs to, has no associated product reader
           @see ProductReader#readBandRasterData(Band, int, int, int, int, ProductData, com.bc.ceres.core.ProgressMonitor)
        """
        TiePointGrid_readRasterData2(self._obj, offsetX, offsetY, width, height, rasterData._obj, pm._obj)
        return

    def readRasterDataFully(self, pm):
        """
           {@inheritDoc}
        """
        TiePointGrid_readRasterDataFully2(self._obj, pm._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData, pm):
        """
           {@inheritDoc}
        """
        TiePointGrid_writeRasterData2(self._obj, offsetX, offsetY, width, height, rasterData._obj, pm._obj)
        return

    def writeRasterDataFully(self, pm):
        """
           {@inheritDoc}
        """
        TiePointGrid_writeRasterDataFully2(self._obj, pm._obj)
        return

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor. 
           
           The method simply calls <code>visitor.visit(this)</code>.
           @param visitor the visitor
        """
        TiePointGrid_acceptVisitor(self._obj, visitor._obj)
        return

    def cloneTiePointGrid(self):
        return TiePointGrid(TiePointGrid_cloneTiePointGrid(self._obj))

    @staticmethod
    def createZenithFromElevationAngleTiePointGrid(elevationAngleGrid):
        return TiePointGrid(TiePointGrid_createZenithFromElevationAngleTiePointGrid(elevationAngleGrid._obj))

    @staticmethod
    def createSubset(sourceTiePointGrid, subsetDef):
        return TiePointGrid(TiePointGrid_createSubset(sourceTiePointGrid._obj, subsetDef._obj))

    def getRasterWidth(self):
        """
           Returns the width of the raster used by this product raster.
           @return the width of the raster
        """
        return TiePointGrid_getRasterWidth(self._obj)

    def getRasterHeight(self):
        """
           Returns the height of the raster used by this product raster.
           @return the height of the raster
        """
        return TiePointGrid_getRasterHeight(self._obj)

    def setModified(self, modified):
        TiePointGrid_setModified(self._obj, modified)
        return

    def getGeoCoding(self):
        """
           Returns the geo-coding of this {@link RasterDataNode}.
           @return the geo-coding
        """
        return GeoCoding(TiePointGrid_getGeoCoding(self._obj))

    def setGeoCoding(self, geoCoding):
        """
           Sets the geo-coding for this {@link RasterDataNode}.
           Also sets the geo-coding of the parent {@link Product} if it has no geo-coding yet.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_GEOCODING}.
           @param geoCoding the new geo-coding
           @see Product#setGeoCoding(GeoCoding)
        """
        TiePointGrid_setGeoCoding(self._obj, geoCoding._obj)
        return

    def getPointing(self):
        """
           Gets a {@link Pointing} if one is available for this raster.
           The methods calls {@link #createPointing()} if a pointing has not been set so far or if its {@link GeoCoding} changed
           since the last creation of this raster's {@link Pointing} instance.
           @return the pointing object, or null if a pointing is not available
        """
        return Pointing(TiePointGrid_getPointing(self._obj))

    def canBeOrthorectified(self):
        """
           Tests if this raster data node can be orthorectified.
           @return true, if so
        """
        return TiePointGrid_canBeOrthorectified(self._obj)

    def getScalingFactor(self):
        """
           Gets the scaling factor which is applied to raw {@link <code>ProductData</code>}. The default value is
           <code>1.0</code> (no factor).
           @return the scaling factor
           @see #isScalingApplied()
        """
        return TiePointGrid_getScalingFactor(self._obj)

    def setScalingFactor(self, scalingFactor):
        """
           Sets the scaling factor which is applied to raw {@link <code>ProductData</code>}.
           @param scalingFactor the scaling factor
           @see #isScalingApplied()
        """
        TiePointGrid_setScalingFactor(self._obj, scalingFactor)
        return

    def getScalingOffset(self):
        """
           Gets the scaling offset which is applied to raw {@link <code>ProductData</code>}. The default value is
           <code>0.0</code> (no offset).
           @return the scaling offset
           @see #isScalingApplied()
        """
        return TiePointGrid_getScalingOffset(self._obj)

    def setScalingOffset(self, scalingOffset):
        """
           Sets the scaling offset which is applied to raw {@link <code>ProductData</code>}.
           @param scalingOffset the scaling offset
           @see #isScalingApplied()
        """
        TiePointGrid_setScalingOffset(self._obj, scalingOffset)
        return

    def isLog10Scaled(self):
        """
           Gets whether or not the {@link <code>ProductData</code>} of this band has a negative binominal distribution and
           thus the common logarithm (base 10) of the values is stored in the raw data. The default value is
           <code>false</code>.
           @return whether or not the data is logging-10 scaled
           @see #isScalingApplied()
        """
        return TiePointGrid_isLog10Scaled(self._obj)

    def setLog10Scaled(self, log10Scaled):
        """
           Sets whether or not the {@link <code>ProductData</code>} of this band has a negative binominal distribution and
           thus the common logarithm (base 10) of the values is stored in the raw data.
           @param log10Scaled whether or not the data is logging-10 scaled
           @see #isScalingApplied()
        """
        TiePointGrid_setLog10Scaled(self._obj, log10Scaled)
        return

    def isScalingApplied(self):
        """
           Tests whether scaling of raw raster data values is applied before they are returned as geophysically meaningful
           pixel values. The methods which return geophysical pixel values are all {@link #getPixels(int, int, int, int, int[])},
           {@link #setPixels(int, int, int, int, int[])}, {@link #readPixels(int, int, int, int, int[])} and
           {@link #writePixels(int, int, int, int, int[])} methods as well as the <code>getPixel&lt;Type&gt;</code> and
           <code>setPixel&lt;Type&gt;</code> methods such as  {@link #getPixelFloat(int, int)} * and
           {@link #setPixelFloat(int, int, float)}.
           @return <code>true</code> if a conversion is applyied to raw data samples before the are retuned.
           @see #getScalingOffset
           @see #getScalingFactor
           @see #isLog10Scaled
        """
        return TiePointGrid_isScalingApplied(self._obj)

    @staticmethod
    def isValidMaskProperty(propertyName):
        """
           Tests if the given name is the name of a property which is relevant for the computation of the valid mask.
           @param propertyName the  name to test
           @return {@code true}, if so.
        """
        return TiePointGrid_isValidMaskProperty(propertyName)

    def isNoDataValueSet(self):
        """
           Tests whether or not a no-data value has been specified. The no-data value is not-specified unless either
           {@link #setNoDataValue(double)} or {@link #setGeophysicalNoDataValue(double)} is called.
           @return true, if so
           @see #isNoDataValueUsed()
           @see #setNoDataValue(double)
        """
        return TiePointGrid_isNoDataValueSet(self._obj)

    def clearNoDataValue(self):
        """
           Clears the no-data value, so that {@link #isNoDataValueSet()} will return <code>false</code>.
        """
        TiePointGrid_clearNoDataValue(self._obj)
        return

    def isNoDataValueUsed(self):
        """
           Tests whether or not the no-data value is used.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return true, if so
           @see #setNoDataValueUsed(boolean)
           @see #isNoDataValueSet()
        """
        return TiePointGrid_isNoDataValueUsed(self._obj)

    def setNoDataValueUsed(self, noDataValueUsed):
        """
           Sets whether or not the no-data value is used.
           If the no-data value is enabled and the no-data value has not been set so far,
           a default no-data value it is set with a value of to zero.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE_USED}.
           @param noDataValueUsed true, if so
           @see #isNoDataValueUsed()
        """
        TiePointGrid_setNoDataValueUsed(self._obj, noDataValueUsed)
        return

    def getNoDataValue(self):
        """
           Gets the no-data value as a primitive <code>double</code>.
           Note that the value returned is NOT necessarily the same as the value returned by
           {@link #getGeophysicalNoDataValue()} because no scaling is applied.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           The method returns <code>0.0</code>, if no no-data value has been specified so far.
           @return the no-data value. It is returned as a <code>double</code> in order to cover all other numeric types.
           @see #setNoDataValue(double)
           @see #isNoDataValueSet()
        """
        return TiePointGrid_getNoDataValue(self._obj)

    def setNoDataValue(self, noDataValue):
        """
           Sets the no-data value as a primitive <code>double</code>.
           Note that the given value is related to the "raw", un-scaled raster data.
           In order to set the geophysical, scaled no-data value use the method
           {@link #setGeophysicalNoDataValue(double)}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE}.
           @param noDataValue the no-data value. It is passed as a <code>double</code> in order to cover all other numeric types.
           @see #getNoDataValue()
           @see #isNoDataValueSet()
        """
        TiePointGrid_setNoDataValue(self._obj, noDataValue)
        return

    def getGeophysicalNoDataValue(self):
        """
           Gets the geophysical no-data value which is simply the scaled "raw" no-data value
           returned by {@link #getNoDataValue()}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return the geophysical no-data value
           @see #setGeophysicalNoDataValue(double)
        """
        return TiePointGrid_getGeophysicalNoDataValue(self._obj)

    def setGeophysicalNoDataValue(self, noDataValue):
        """
           Sets the geophysical no-data value which is simply the scaled "raw" no-data value
           returned by {@link #getNoDataValue()}.
           The no-data value is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_NO_DATA_VALUE}.
           @param noDataValue the new geophysical no-data value
           @see #setGeophysicalNoDataValue(double)
           @see #isNoDataValueSet()
        """
        TiePointGrid_setGeophysicalNoDataValue(self._obj, noDataValue)
        return

    def getValidPixelExpression(self):
        """
           Gets the expression that is used to determine whether a pixel is valid or not.
           For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return the valid mask expression.
        """
        return TiePointGrid_getValidPixelExpression(self._obj)

    def setValidPixelExpression(self, validPixelExpression):
        """
           Sets the expression that is used to determine whether a pixel is valid or not.
           The valid-pixel expression is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           On property change, the method calls {@link #fireProductNodeChanged(String)} with the property
           name {@link #PROPERTY_NAME_VALID_PIXEL_EXPRESSION}.
           @param validPixelExpression the valid mask expression, can be null
        """
        TiePointGrid_setValidPixelExpression(self._obj, validPixelExpression)
        return

    def isValidMaskUsed(self):
        """
           Tests whether or not this raster data node uses a data-mask in order to determine valid pixels. The method returns
           true if either {@link #isValidPixelExpressionSet()} or {@link #isNoDataValueUsed()} returns true.
           The data-mask is used to determine valid pixels. For more information
           on valid pixels, please refer to the documentation of the {@link #isPixelValid(int, int, javax.media.jai.ROI)}
           method.
           @return true, if so
        """
        return TiePointGrid_isValidMaskUsed(self._obj)

    def resetValidMask(self):
        """
           Resets the valid mask of this raster.
           The mask will be lazily regenerated when requested the next time.
        """
        TiePointGrid_resetValidMask(self._obj)
        return

    def getValidMaskExpression(self):
        """
           Gets the expression used for the computation of the mask which identifies valid pixel values.
           It recognizes the value of the {@link #getNoDataValue() noDataValue} and the
           {@link #getValidPixelExpression() validPixelExpression} properties, if any.
           The method returns {@code null},  if none of these properties are set.
           @return The expression used for the computation of the mask which identifies valid pixel values,
           or {@code null}.
           @see #getValidPixelExpression()
           @see #getNoDataValue()
        """
        return TiePointGrid_getValidMaskExpression(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           {@inheritDoc}
        """
        TiePointGrid_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def hasRasterData(self):
        """
           Returns true if the raster data of this <code>RasterDataNode</code> is loaded or elsewhere available, otherwise
           false.
           @return true, if so.
        """
        return TiePointGrid_hasRasterData(self._obj)

    def getRasterData(self):
        """
           Gets the raster data for this dataset. If the data has'nt been loaded so far the method returns
           <code>null</code>.
           @return the raster data for this band, or <code>null</code> if data has not been loaded
        """
        return ProductData(TiePointGrid_getRasterData(self._obj))

    def setRasterData(self, rasterData):
        """
           Sets the raster data of this dataset.
           
            Note that this method does not copy data at all. If the supplied raster data is compatible with this product
           raster, then simply its reference is stored. Modifications in the supplied raster data will also affect this
           dataset's data!
           @param rasterData the raster data for this dataset
           @see #getRasterData()
        """
        TiePointGrid_setRasterData(self._obj, rasterData._obj)
        return

    def loadRasterData(self):
        """
           @throws java.io.IOException if an I/O error occurs
           @see #loadRasterData(com.bc.ceres.core.ProgressMonitor)
        """
        TiePointGrid_loadRasterData1(self._obj)
        return

    def loadRasterData(self, pm):
        """
           Loads the raster data for this <code>RasterDataNode</code>. After this method has been called successfully,
           <code>hasRasterData()</code> should always return <code>true</code> and <code>getRasterData()</code> should
           always return a valid <code>ProductData</code> instance with at least <code>getRasterWidth()*getRasterHeight()</code>
           elements (samples).
           
           The default implementation of this method does nothing.
           @param pm a monitor to inform the user about progress
           @throws IOException if an I/O error occurs
           @see #unloadRasterData()
        """
        TiePointGrid_loadRasterData2(self._obj, pm._obj)
        return

    def unloadRasterData(self):
        """
           Un-loads the raster data for this <code>RasterDataNode</code>.
           
           It is up to the implementation whether after this method has been called successfully, the
           <code>hasRasterData()</code> method returns <code>false</code> or <code>true</code>.
           
           The default implementation of this method does nothing.
           @see #loadRasterData()
        """
        TiePointGrid_unloadRasterData(self._obj)
        return

    def isPixelValid(self, x, y):
        """
           Checks whether or not the pixel located at (x,y) is valid.
           A pixel is assumed to be valid either if  {@link #getValidMaskImage() validMaskImage} is null or
           or if the bit corresponding to (x,y) is set within the returned mask image.
           
           <i>Note: Implementation changed by Norman (2011-08-09) in order to increase performance since
           a synchronised block was used due to problem with the JAI ROI class that has been used in
           the former implementation.</i>
           @param x the X co-ordinate of the pixel location
           @param y the Y co-ordinate of the pixel location
           @return <code>true</code> if the pixel is valid
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
           @see #isPixelValid(int, int, javax.media.jai.ROI)
           @see #setNoDataValueUsed(boolean)
           @see #setNoDataValue(double)
           @see #setValidPixelExpression(String)
        """
        return TiePointGrid_isPixelValid2(self._obj, x, y)

    def getSampleInt(self, x, y):
        """
           Gets a geo-physical sample value at the given pixel coordinate as {@code int} value.
           
           <i>Note: This method does not belong to the public API.
           It has been added by Norman (2011-08-09) in order to perform performance tests.</i>
           @param x pixel X coordinate
           @param y pixel Y coordinate
           @return The geo-physical sample value.
        """
        return TiePointGrid_getSampleInt(self._obj, x, y)

    def getSampleFloat(self, x, y):
        """
           Gets a geo-physical sample value at the given pixel coordinate as {@code float} value.
           
           <i>Note: This method does not belong to the public API.
           It has been added by Norman (2011-08-09) in order to perform performance tests.</i>
           @param x pixel X coordinate
           @param y pixel Y coordinate
           @return The geo-physical sample value.
        """
        return TiePointGrid_getSampleFloat(self._obj, x, y)

    def isPixelValid(self, pixelIndex):
        """
           Checks whether or not the pixel located at (x,y) is valid.
           A pixel is assumed to be valid either if  {@link #getValidMaskImage() validMaskImage} is null or
           or if the bit corresponding to (x,y) is set within the returned mask image.
           @param pixelIndex the linear pixel index in the range 0 to width * height - 1
           @return <code>true</code> if the pixel is valid
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
           @see #isPixelValid(int, int, javax.media.jai.ROI)
           @see #setNoDataValueUsed(boolean)
           @see #setNoDataValue(double)
           @see #setValidPixelExpression(String)
        """
        return TiePointGrid_isPixelValid1(self._obj, pixelIndex)

    def isPixelValid(self, x, y, roi):
        """
           Checks whether or not the pixel located at (x,y) is valid.
           The method first test whether a pixel is valid by using the {@link #isPixelValid(int, int)} method,
           and secondly, if the pixel is within the ROI (if any).
           @param x   the X co-ordinate of the pixel location
           @param y   the Y co-ordinate of the pixel location
           @param roi the ROI, if null the method returns {@link #isPixelValid(int, int)}
           @return <code>true</code> if the pixel is valid
           @throws ArrayIndexOutOfBoundsException if the co-ordinates are not in bounds
           @see #isPixelValid(int, int)
           @see #setNoDataValueUsed(boolean)
           @see #setNoDataValue(double)
           @see #setValidPixelExpression(String)
        """
        return TiePointGrid_isPixelValid3(self._obj, x, y, roi._obj)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, int[], ProgressMonitor)
        """
        return TiePointGrid_getPixels5(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, float[], ProgressMonitor)
        """
        return TiePointGrid_getPixels3(self._obj, x, y, w, h, pixels)

    def getPixels(self, x, y, w, h, pixels):
        """
           @see #getPixels(int, int, int, int, double[], ProgressMonitor)
        """
        return TiePointGrid_getPixels1(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, int[], ProgressMonitor)
        """
        return TiePointGrid_readPixels5(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, float[], ProgressMonitor)
        """
        return TiePointGrid_readPixels3(self._obj, x, y, w, h, pixels)

    def readPixels(self, x, y, w, h, pixels):
        """
           @see #readPixels(int, int, int, int, double[], ProgressMonitor)
        """
        return TiePointGrid_readPixels1(self._obj, x, y, w, h, pixels)

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, int[], ProgressMonitor)
        """
        TiePointGrid_writePixels5(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, float[], ProgressMonitor)
        """
        TiePointGrid_writePixels3(self._obj, x, y, w, h, pixels)
        return

    def writePixels(self, x, y, w, h, pixels):
        """
           @see #writePixels(int, int, int, int, double[], ProgressMonitor)
        """
        TiePointGrid_writePixels1(self._obj, x, y, w, h, pixels)
        return

    def readValidMask(self, x, y, w, h, validMask):
        return TiePointGrid_readValidMask(self._obj, x, y, w, h, validMask)

    def readRasterDataFully(self):
        """
           @throws java.io.IOException if an I/O error occurs
           @see #readRasterDataFully(ProgressMonitor)
        """
        TiePointGrid_readRasterDataFully1(self._obj)
        return

    def readRasterData(self, offsetX, offsetY, width, height, rasterData):
        """
           Reads raster data from the node's associated data source into the given data
           buffer.
           @param offsetX    the X-offset in the raster co-ordinates where reading starts
           @param offsetY    the Y-offset in the raster co-ordinates where reading starts
           @param width      the width of the raster data buffer
           @param height     the height of the raster data buffer
           @param rasterData a raster data buffer receiving the pixels to be read
           @throws java.io.IOException      if an I/O error occurs
           @throws IllegalArgumentException if the raster is null
           @throws IllegalStateException    if this product raster was not added to a product so far, or if the product to
           which this product raster belongs to, has no associated product reader
           @see ProductReader#readBandRasterData(Band, int, int, int, int, ProductData, com.bc.ceres.core.ProgressMonitor)
        """
        TiePointGrid_readRasterData1(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def writeRasterDataFully(self):
        TiePointGrid_writeRasterDataFully1(self._obj)
        return

    def writeRasterData(self, offsetX, offsetY, width, height, rasterData):
        TiePointGrid_writeRasterData1(self._obj, offsetX, offsetY, width, height, rasterData._obj)
        return

    def createCompatibleRasterData(self):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>getRasterWidth()*getRasterHeight()</code> elements of a compatible data type.
           @return raster data compatible with this product raster
           @see #createCompatibleSceneRasterData
        """
        return ProductData(TiePointGrid_createCompatibleRasterData1(self._obj))

    def createCompatibleSceneRasterData(self):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>getBandOutputRasterWidth()*getBandOutputRasterHeight()</code> elements of a compatible data type.
           @return raster data compatible with this product raster
           @see #createCompatibleRasterData
        """
        return ProductData(TiePointGrid_createCompatibleSceneRasterData(self._obj))

    def createCompatibleRasterData(self, width, height):
        """
           Creates raster data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>width*height</code> elements of a compatible data type.
           @param width  the width of the raster data to be created
           @param height the height of the raster data to be created
           @return raster data compatible with this product raster
           @see #createCompatibleRasterData
           @see #createCompatibleSceneRasterData
        """
        return ProductData(TiePointGrid_createCompatibleRasterData2(self._obj, width, height))

    def isCompatibleRasterData(self, rasterData, w, h):
        """
           Tests whether the given parameters specify a compatible raster or not.
           @param rasterData the raster data
           @param w          the raster width
           @param h          the raster height
           @return {@code true} if so
        """
        return TiePointGrid_isCompatibleRasterData(self._obj, rasterData._obj, w, h)

    def checkCompatibleRasterData(self, rasterData, w, h):
        """
           Throws an <code>IllegalArgumentException</code> if the given parameters dont specify a compatible raster.
           @param rasterData the raster data
           @param w          the raster width
           @param h          the raster height
        """
        TiePointGrid_checkCompatibleRasterData(self._obj, rasterData._obj, w, h)
        return

    def hasIntPixels(self):
        """
           Determines whether this raster data node contains integer samples.
           @return true if this raster data node contains integer samples.
        """
        return TiePointGrid_hasIntPixels(self._obj)

    def createTransectProfileData(self, shape):
        """
           Creates a transect profile for the given shape (-outline).
           @param shape the shape
           @return the profile data
           @throws IOException if an I/O error occurs
        """
        return TransectProfileData(TiePointGrid_createTransectProfileData(self._obj, shape._obj))

    def getImageInfo(self):
        """
           Gets the image information for image display.
           @return the image info or null
        """
        return ImageInfo(TiePointGrid_getImageInfo1(self._obj))

    def setImageInfo(self, imageInfo):
        """
           Sets the image information for image display.
           @param imageInfo the image info, can be null
        """
        TiePointGrid_setImageInfo(self._obj, imageInfo._obj)
        return

    def fireImageInfoChanged(self):
        """
           Notifies listeners that the image (display) information has changed.
        """
        TiePointGrid_fireImageInfoChanged(self._obj)
        return

    def getImageInfo(self, pm):
        """
           Returns the image information for this raster data node.
           
           The method simply returns the value of <code>ensureValidImageInfo(null, ProgressMonitor.NULL)</code>.
           @param pm A progress monitor.
           @return A valid image information instance.
           @see #getImageInfo(double[], ProgressMonitor)
        """
        return ImageInfo(TiePointGrid_getImageInfo2(self._obj, pm._obj))

    def getImageInfo(self, histoSkipAreas, pm):
        """
           Gets the image creation information.
           
           If no image information has been assigned before, the <code>{@link #createDefaultImageInfo(double[], com.bc.ceres.core.ProgressMonitor)}</code> method is
           called with the given parameters passed to this method.
           @param histoSkipAreas Only used, if new image info is created (see <code>{@link #createDefaultImageInfo(double[], com.bc.ceres.core.ProgressMonitor)}</code>
           method).
           @param pm             A progress monitor.
           @return The image creation information.
        """
        return ImageInfo(TiePointGrid_getImageInfo3(self._obj, histoSkipAreas, pm._obj))

    def createDefaultImageInfo(self, histoSkipAreas, pm):
        """
           Creates a default image information instance.
           
           An <code>IllegalStateException</code> is thrown in the case that this raster data node has no raster data.
           @param histoSkipAreas the left (at index 0) and right (at index 1) normalized areas of the raster data
           histogram to be excluded when determining the value range for a linear constrast
           stretching. Can be <code>null</code>, in this case <code>{0.01, 0.04}</code> resp. 5% of
           the entire area is skipped.
           @param pm             a monitor to inform the user about progress
           @return a valid image information instance, never <code>null</code>.
        """
        return ImageInfo(TiePointGrid_createDefaultImageInfo1(self._obj, histoSkipAreas, pm._obj))

    def createDefaultImageInfo(self, histoSkipAreas, histogram):
        """
           Creates an instance of a default image information.
           
           An <code>IllegalStateException</code> is thrown in the case that this raster data node has no raster data.
           @param histoSkipAreas the left (at index 0) and right (at index 1) normalized areas of the raster data
           histogram to be excluded when determining the value range for a linear constrast
           stretching. Can be <code>null</code>, in this case <code>{0.01, 0.04}</code> resp. 5% of
           the entire area is skipped.
           @param histogram      the histogram to create the image information.
           @return a valid image information instance, never <code>null</code>.
        """
        return ImageInfo(TiePointGrid_createDefaultImageInfo2(self._obj, histoSkipAreas, histogram._obj))

    def getOverlayMaskGroup(self):
        """
           @return The overlay mask group.
        """
        return ProductNodeGroup(TiePointGrid_getOverlayMaskGroup(self._obj))

    def createColorIndexedImage(self, pm):
        """
           Creates an image for this raster data node. The method simply returns <code>ProductUtils.createColorIndexedImage(this,
           null)</code>.
           @param pm a monitor to inform the user about progress
           @return a greyscale/palette-based image for this raster data node
           @throws IOException if the raster data is not loaded so far and reload causes an I/O error
           @see #setImageInfo(ImageInfo)
        """
        return BufferedImage(TiePointGrid_createColorIndexedImage(self._obj, pm._obj))

    def createRgbImage(self, pm):
        """
           Creates an RGB image for this raster data node.
           @param pm a monitor to inform the user about progress
           @return a greyscale/palette-based image for this raster data node
           @throws IOException if the raster data is not loaded so far and reload causes an I/O error
           @see #setImageInfo(ImageInfo)
        """
        return BufferedImage(TiePointGrid_createRgbImage(self._obj, pm._obj))

    def quantizeRasterData(self, newMin, newMax, gamma, pm):
        return TiePointGrid_quantizeRasterData1(self._obj, newMin, newMax, gamma, pm._obj)

    def quantizeRasterData(self, newMin, newMax, gamma, samples, offset, stride, pm):
        TiePointGrid_quantizeRasterData2(self._obj, newMin, newMax, gamma, samples, offset, stride, pm._obj)
        return

    def createPixelValidator(self, lineOffset, roi):
        """
           Creates a validator which can be used to validate indexes of pixels in a flat raster data buffer.
           @param lineOffset the absolute line offset, zero based
           @param roi        an optional ROI
           @return a new validator instance, never null
           @throws IOException if an I/O error occurs
        """
        return IndexValidator(TiePointGrid_createPixelValidator(self._obj, lineOffset, roi._obj))

    def scale(self, v):
        """
           Applies the scaling <code>v * scalingFactor + scalingOffset</code> the the given input value. If the
           <code>log10Scaled</code> property is true, the result is taken to the power of 10 <i>after</i> the actual
           scaling.
           @param v the input value
           @return the scaled value
        """
        return TiePointGrid_scale(self._obj, v)

    def scaleInverse(self, v):
        """
           Applies the inverse scaling <code>(v - scalingOffset) / scalingFactor</code> the the given input value. If the
           <code>log10Scaled</code> property is true, the common logarithm is applied to the input <i>before</i> the actual
           scaling.
           @param v the input value
           @return the scaled value
        """
        return TiePointGrid_scaleInverse(self._obj, v)

    def getPixelString(self, x, y):
        """
           Returns the pixel located at (x,y) as a string value.
           @param x the X co-ordinate of the pixel location
           @param y the Y co-ordinate of the pixel location
           @return the pixel value at (x,y) as string or an error message text
        """
        return TiePointGrid_getPixelString(self._obj, x, y)

    def isSourceImageSet(self):
        """
           Returns whether the source image is set on this {@code RasterDataNode}.
           @return whether the source image is set.
           @see #getSourceImage()
           @see #setSourceImage(java.awt.image.RenderedImage)
           @see #setSourceImage(com.bc.ceres.glevel.MultiLevelImage)
           @see #createSourceImage()
        """
        return TiePointGrid_isSourceImageSet(self._obj)

    def getSourceImage(self):
        """
           Gets the source image associated with this {@code RasterDataNode}.
           @return The source image. Never {@code null}. In the case that {@link #isSourceImageSet()} returns {@code false},
           the method {@link #createSourceImage()} will be called in order to set and return a valid source image.
           @see #createSourceImage()
           @see #isSourceImageSet()
        """
        return MultiLevelImage(TiePointGrid_getSourceImage(self._obj))

    def setSourceImage(self, sourceImage):
        """
           Sets the source image associated with this {@code RasterDataNode}.
           @param sourceImage The source image.
           Can be {@code null}. If so, {@link #isSourceImageSet()} will return {@code false}.
        """
        TiePointGrid_setSourceImage2(self._obj, sourceImage._obj)
        return

    def setSourceImage(self, sourceImage):
        """
           Sets the source image associated with this {@code RasterDataNode}.
           @param sourceImage The source image.
           Can be {@code null}. If so, {@link #isSourceImageSet()} will return {@code false}.
        """
        TiePointGrid_setSourceImage1(self._obj, sourceImage._obj)
        return

    def isGeophysicalImageSet(self):
        """
           Returns whether the geophysical image is set on this {@code RasterDataNode}.
           
           This method belongs to preliminary API and may be removed or changed in the future.
           @return whether the geophysical image is set.
        """
        return TiePointGrid_isGeophysicalImageSet(self._obj)

    def getGeophysicalImage(self):
        """
           @return The geophysical source image.
        """
        return MultiLevelImage(TiePointGrid_getGeophysicalImage(self._obj))

    def isValidMaskImageSet(self):
        """
           Returns wether the valid mask image is set on this {@code RasterDataNode}.
           @return Wether the source image is set.
        """
        return TiePointGrid_isValidMaskImageSet(self._obj)

    def getValidMaskImage(self):
        """
           Gets the valid-mask image associated with this {@code RasterDataNode}.
           @return The rendered image.
        """
        return MultiLevelImage(TiePointGrid_getValidMaskImage(self._obj))

    def isStxSet(self):
        return TiePointGrid_isStxSet(self._obj)

    def getStx(self):
        """
           Gets the statistics. If statistcs are not yet available,
           the method will compute (possibly inaccurate) statistics and return those.
           
           If accurate statistics are required, the {@link #getStx(boolean, com.bc.ceres.core.ProgressMonitor)}
           shall be used instead.
           
           This method belongs to preliminary API and may be removed or changed in the future.
           @return The statistics.
           @see #getStx(boolean, com.bc.ceres.core.ProgressMonitor)
           @see #setStx(Stx)
        """
        return Stx(TiePointGrid_getStx1(self._obj))

    def getStx(self, accurate, pm):
        """
           Gets the statistics.
           If the statistics have not been set before they are computed using the given progress monitor {@code pm} and then set.
           This method belongs to preliminary API and may be removed or changed in the future.
           @param accurate If true, accurate statistics are computed.
           @param pm       A progress monitor which is used to compute the new statistics, if required.
           @return The statistics.
        """
        return Stx(TiePointGrid_getStx2(self._obj, accurate, pm._obj))

    def setStx(self, stx):
        """
           Sets the statistics. It is the responsibility of the caller to ensure that the given statistics
           are really related to this {@code RasterDataNode}'s raster data.
           The method fires a property change event for the property {@link #PROPERTY_NAME_STX}.
           This method belongs to preliminary API and may be removed or changed in the future.
           @param stx The statistics.
        """
        TiePointGrid_setStx(self._obj, stx._obj)
        return

    def getValidShape(self):
        """
           Gets the shape of the area where this raster data contains valid samples.
           The method returns <code>null</code>, if the entire raster contains valid samples.
           @return The shape of the area where the raster data has samples, can be {@code null}.
        """
        return Shape(TiePointGrid_getValidShape(self._obj))

    def getDataType(self):
        """
           Gets the data type of this data node.
           @return the data type which is always one of the multiple <code>ProductData.TYPE_<i>X</i></code> constants
        """
        return TiePointGrid_getDataType(self._obj)

    def getNumDataElems(self):
        """
           Gets the number of data elements in this data node.
        """
        return TiePointGrid_getNumDataElems(self._obj)

    def setData(self, data):
        """
           Sets the data of this data node.
        """
        TiePointGrid_setData(self._obj, data._obj)
        return

    def getData(self):
        """
           Gets the data of this data node.
        """
        return ProductData(TiePointGrid_getData(self._obj))

    def setDataElems(self, elems):
        """
           Sets the data elements of this data node.
           @see ProductData#setElems(Object)
        """
        TiePointGrid_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        """
           Gets the data elements of this data node.
           @see ProductData#getElems()
        """
        return Object(TiePointGrid_getDataElems(self._obj))

    def getDataElemSize(self):
        """
           Gets the data element size in bytes.
           @see ProductData#getElemSize(int)
        """
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
        """
           Fires a node data changed event. This method is called after the data of this data node changed.
        """
        TiePointGrid_fireProductNodeDataChanged(self._obj)
        return

    def getRawStorageSize(self, subsetDef):
        """
           Gets the estimated size in bytes of this product node.
           @param subsetDef if not <code>null</code> the subset may limit the size returned
           @return the size in bytes.
        """
        return TiePointGrid_getRawStorageSize2(self._obj, subsetDef._obj)

    def createCompatibleProductData(self, numElems):
        """
           Creates product data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>numElems</code> elements of a compatible data type.
           @param numElems the number of elements, must not be less than one
           @return product data compatible with this data node
        """
        return ProductData(TiePointGrid_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(TiePointGrid_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return TiePointGrid_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        TiePointGrid_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return TiePointGrid_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        TiePointGrid_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return TiePointGrid_isModified(self._obj)

    def toString(self):
        return TiePointGrid_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return TiePointGrid_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(TiePointGrid_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(TiePointGrid_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(TiePointGrid_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return TiePointGrid_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return TiePointGrid_getProductRefString(self._obj)

    def getRawStorageSize(self):
        """
           Gets an estimated, raw storage size in bytes of this product node.
           @return the size in bytes.
        """
        return TiePointGrid_getRawStorageSize1(self._obj)

    def fireProductNodeChanged(self, propertyName):
        TiePointGrid_fireProductNodeChanged1(self._obj, propertyName)
        return

    def fireProductNodeChanged(self, propertyName, oldValue, newValue):
        TiePointGrid_fireProductNodeChanged2(self._obj, propertyName, oldValue._obj, newValue._obj)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        TiePointGrid_removeFromFile(self._obj, productWriter._obj)
        return


class SimpleFeature:
    def __init__(self, obj):
        self._obj = obj


"""
The <code>ProductSubsetDef</code> class describes a subset or portion of a remote sensing data product.

 Subsets can be spatial or spectral or both. A spatial subset is given through a rectangular region in pixels. The
spectral subset as a list of band (or channel) names.
"""
class ProductSubsetDef:
    def __init__(self, obj):
        self._obj = obj


"""
An interface that supports writing a complete data product tree and single band rasters.
@see ProductReader
"""
class ProductWriter:
    def __init__(self, obj):
        self._obj = obj

    def getWriterPlugIn(self):
        """
           Returns the plug-in which created this product writer.
           @return the product writer plug-in, should never be <code>null</code>
        """
        return ProductWriterPlugIn(ProductWriter_getWriterPlugIn(self._obj))

    def getOutput(self):
        """
           Retrives the current output destination object. Thie return value might be <code>null</code> if the
           <code>setOutput</code> has not been called so far.
           @return the output
        """
        return Object(ProductWriter_getOutput(self._obj))

    def writeProductNodes(self, product, output):
        """
           Writes the in-memory representation of a data product.
           
            Whether the band data - the actual pixel values - is written out immediately or later when pixels are
           flushed, is up to the implementation.
           @param product the in-memory representation of the data product
           @param output  an object representing a valid output for this writer, might be a <code>ImageInputStream</code> or
           other <code>Object</code> to use for future decoding.
           @throws IllegalArgumentException if <code>output</code> is <code>null</code> or it's type is not one of the
           supported output sources.
           @throws IOException              if an I/O error occurs
        """
        ProductWriter_writeProductNodes(self._obj, product._obj, output._obj)
        return

    def writeBandRasterData(self, sourceBand, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer, pm):
        """
           Writes raster data from the given in-memory source buffer into the data sink specified by the given source band
           and region.
           
           <h3>Source band</h3> The source band is used to identify the data sink in which this method transfers the sample
           values given in the source buffer. The method does not modify the pixel data of the given source band at all.
           
           <h3>Source buffer</h3> The first element of the source buffer corresponds to the given <code>sourceOffsetX</code>
           and <code>sourceOffsetY</code> of the source region. These parameters are an offset within the band's raster data
           and <b>not</b> an offset within the source buffer.<br> The number of elements in the buffer must be exactly be
           <code>sourceWidth * sourceHeight</code>. The pixel values to be writte are considered to be stored in
           line-by-line order, so the raster X co-ordinate varies faster than the Y.
           
           <h3>Source region</h3> The given destination region specified by the <code>sourceOffsetX</code>,
           <code>sourceOffsetY</code>, <code>sourceWidth</code> and <code>sourceHeight</code> parameters is given in the
           source band's raster co-ordinates. These co-ordinates are identical with the destination raster co-ordinates
           since product writers do not support spectral or spatial subsets.
           @param sourceBand    the source band which identifies the data sink to which to write the sample values
           @param sourceOffsetX the X-offset in the band's raster co-ordinates
           @param sourceOffsetY the Y-offset in the band's raster co-ordinates
           @param sourceWidth   the width of region to be written given in the band's raster co-ordinates
           @param sourceHeight  the height of region to be written given in the band's raster co-ordinates
           @param sourceBuffer  the source buffer which provides the sample values to be written
           @param pm            a monitor to inform the user about progress
           @throws IOException              if an I/O error occurs
           @throws IllegalArgumentException if the number of elements source buffer not equals <code>sourceWidth *
           sourceHeight</code> or the source region is out of the band's raster
           @see Band#getRasterWidth()
           @see Band#getRasterHeight()
        """
        ProductWriter_writeBandRasterData(self._obj, sourceBand._obj, sourceOffsetX, sourceOffsetY, sourceWidth, sourceHeight, sourceBuffer._obj, pm._obj)
        return

    def flush(self):
        """
           Writes all data in memory to the data sink(s) associated with this writer.
           @throws IOException if an I/O error occurs
        """
        ProductWriter_flush(self._obj)
        return

    def close(self):
        """
           Closes all output streams currently open. A concrete implementation should call <code>flush</code> before
           performing the actual close-operation.
           @throws IOException if an I/O error occurs
        """
        ProductWriter_close(self._obj)
        return

    def shouldWrite(self, node):
        """
           Returns wether the given product node is to be written.
           @param node the product node
           @return <code>true</code> if so
        """
        return ProductWriter_shouldWrite(self._obj, node._obj)

    def isIncrementalMode(self):
        """
           Returns whether this product writer writes only modified product nodes.
           @return <code>true</code> if so
        """
        return ProductWriter_isIncrementalMode(self._obj)

    def setIncrementalMode(self, enabled):
        """
           Enables resp. disables incremental writing of this product writer. By default, a reader should enable progress
           listening.
           @param enabled enables or disables progress listening.
        """
        ProductWriter_setIncrementalMode(self._obj, enabled)
        return

    def deleteOutput(self):
        """
           Complete deletes the physical representation of the given product from the file system.
           @throws IOException if an I/O error occurs
        """
        ProductWriter_deleteOutput(self._obj)
        return

    def removeBand(self, band):
        """
           Physically deletes a <code>Band</code> in a product writer's output.
           @param band The band to delete.
        """
        ProductWriter_removeBand(self._obj, band._obj)
        return


"""
A <code>MetadataAttribute</code> is part of a <code>{@link MetadataElement}</code> and represents a key/value pair.
"""
class MetadataAttribute:
    def __init__(self, obj):
        self._obj = obj

    @staticmethod
    def newMetadataAttribute(name, data, readOnly):
        return MetadataAttribute(MetadataAttribute_newMetadataAttribute(name, data._obj, readOnly))

    def getParentElement(self):
        return MetadataElement(MetadataAttribute_getParentElement(self._obj))

    def equals(self, object):
        return MetadataAttribute_equals(self._obj, object._obj)

    def acceptVisitor(self, visitor):
        """
           Accepts the given visitor. This method implements the well known 'Visitor' design pattern of the gang-of-four.
           The visitor pattern allows to define new operations on the product data model without the need to add more code
           to it. The new operation is implemented by the visitor.
           
           The method simply calls <code>visitor.visit(this)</code>.
           @param visitor the visitor
        """
        MetadataAttribute_acceptVisitor(self._obj, visitor._obj)
        return

    def createDeepClone(self):
        return MetadataAttribute(MetadataAttribute_createDeepClone(self._obj))

    def getDataType(self):
        """
           Gets the data type of this data node.
           @return the data type which is always one of the multiple <code>ProductData.TYPE_<i>X</i></code> constants
        """
        return MetadataAttribute_getDataType(self._obj)

    def isFloatingPointType(self):
        """
           Tests whether the data type of this node is a floating point type.
           @return true, if so
        """
        return MetadataAttribute_isFloatingPointType(self._obj)

    def getNumDataElems(self):
        """
           Gets the number of data elements in this data node.
        """
        return MetadataAttribute_getNumDataElems(self._obj)

    def setData(self, data):
        """
           Sets the data of this data node.
        """
        MetadataAttribute_setData(self._obj, data._obj)
        return

    def getData(self):
        """
           Gets the data of this data node.
        """
        return ProductData(MetadataAttribute_getData(self._obj))

    def setDataElems(self, elems):
        """
           Sets the data elements of this data node.
           @see ProductData#setElems(Object)
        """
        MetadataAttribute_setDataElems(self._obj, elems._obj)
        return

    def getDataElems(self):
        """
           Gets the data elements of this data node.
           @see ProductData#getElems()
        """
        return Object(MetadataAttribute_getDataElems(self._obj))

    def getDataElemSize(self):
        """
           Gets the data element size in bytes.
           @see ProductData#getElemSize(int)
        """
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
        """
           Fires a node data changed event. This method is called after the data of this data node changed.
        """
        MetadataAttribute_fireProductNodeDataChanged(self._obj)
        return

    def dispose(self):
        """
           Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
           allow the garbage collector to perform a vanilla job.
           
           This method should be called only if it is for sure that this object instance will never be used again. The
           results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
           
           Overrides of this method should always call <code>super.dispose();</code> after disposing this instance.
        """
        MetadataAttribute_dispose(self._obj)
        return

    def createCompatibleProductData(self, numElems):
        """
           Creates product data that is compatible to this dataset's data type. The data buffer returned contains exactly
           <code>numElems</code> elements of a compatible data type.
           @param numElems the number of elements, must not be less than one
           @return product data compatible with this data node
        """
        return ProductData(MetadataAttribute_createCompatibleProductData(self._obj, numElems))

    def getOwner(self):
        """
           @return The owner node of this node.
        """
        return ProductNode(MetadataAttribute_getOwner(self._obj))

    def getName(self):
        """
           @return This node's name.
        """
        return MetadataAttribute_getName(self._obj)

    def setName(self, name):
        """
           Sets this product's name.
           @param name The name.
        """
        MetadataAttribute_setName(self._obj, name)
        return

    def getDescription(self):
        """
           Returns a short textual description for this products node.
           @return a description or <code>null</code>
        """
        return MetadataAttribute_getDescription(self._obj)

    def setDescription(self, description):
        """
           Sets a short textual description for this products node.
           @param description a description, can be <code>null</code>
        """
        MetadataAttribute_setDescription(self._obj, description)
        return

    def isModified(self):
        """
           Returns whether or not this node is modified.
           @return <code>true</code> if so
        """
        return MetadataAttribute_isModified(self._obj)

    def setModified(self, modified):
        """
           Sets this node's modified flag.
           
           If the modified flag changes to true and this node has an owner, the owner's modified flag is also set to
           true.
           @param modified whether or not this node is beeing marked as modified.
           @see Product#fireNodeChanged
        """
        MetadataAttribute_setModified(self._obj, modified)
        return

    def toString(self):
        return MetadataAttribute_toString(self._obj)

    @staticmethod
    def isValidNodeName(name):
        """
           Tests whether the given name is valid name for a node.
           A valid node name must not start with a dot. Also a valid node name must not contain
           any of the character  <code>\/:*?"&lt;&gt;|</code>
           @param name the name to test
           @return <code>true</code> if the name is a valid node identifier, <code>false</code> otherwise
        """
        return MetadataAttribute_isValidNodeName(name)

    def getProduct(self):
        """
           Returns the product to which this node belongs to.
           @return the product, or <code>null</code> if this node was not owned by a product at the time this method was
           called
        """
        return Product(MetadataAttribute_getProduct(self._obj))

    def getProductReader(self):
        """
           Returns the product reader for the product to which this node belongs to.
           @return the product reader, or <code>null</code> if no such exists
        """
        return ProductReader(MetadataAttribute_getProductReader(self._obj))

    def getProductWriter(self):
        """
           Returns the product writer for the product to which this node belongs to.
           @return the product writer, or <code>null</code> if no such exists
        """
        return ProductWriter(MetadataAttribute_getProductWriter(self._obj))

    def getDisplayName(self):
        """
           Returns this node's display name. The display name is the product reference string with the node name appended.
           Example: The string <code>"[2] <i>node-name</i>"</code> means node <code><i>node-name</i></code> of the
           product with the reference number <code>2</code>.
           @return this node's name with a product prefix <br>or this node's name only if this node's product prefix is
           <code>null</code>
           @see #getProductRefString
        """
        return MetadataAttribute_getDisplayName(self._obj)

    def getProductRefString(self):
        """
           Gets the product reference string. The product reference string is the product reference number enclosed in
           square brackets. Example: The string <code>"[2]"</code> stands for a product with the reference number
           <code>2</code>.
           @return the product reference string. <br>or <code>null</code> if this node has no product <br>or
           <code>null</code> if its product reference number was inactive
        """
        return MetadataAttribute_getProductRefString(self._obj)

    def updateExpression(self, oldExternalName, newExternalName):
        """
           Asks a product node to replace all occurences of and references to the node name
           given by {@code oldExternalName} with {@code oldExternalName}. Such references most often occur
           in band arithmetic expressions.
           @param oldExternalName The old node name.
           @param newExternalName The new node name.
        """
        MetadataAttribute_updateExpression(self._obj, oldExternalName, newExternalName)
        return

    def removeFromFile(self, productWriter):
        """
           Physically remove this node from the file associated with the given product writer. The default implementation
           does nothing.
           @param productWriter the product writer to be used to remove this node from the underlying file.
        """
        MetadataAttribute_removeFromFile(self._obj, productWriter._obj)
        return


class ProgressMonitor:
    def __init__(self, obj):
        self._obj = obj


"""
A container which allows to store vector data in the BEAM product model.

This is a preliminary API under construction for BEAM 4.7. Not intended for public use.
@see Product#getVectorDataGroup()
"""
class VectorDataNode:
    def __init__(self, obj):
        self._obj = obj


class GeneralPath:
    def __init__(self, obj):
        self._obj = obj


class ImageInputStream:
    def __init__(self, obj):
        self._obj = obj


