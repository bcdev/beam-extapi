#ifndef BEAMPY_H
#define BEAMPY_H

#ifdef __cplusplus
extern "C" {
#endif

#include "../beampy_carray.h"
#include "../beampy_jobject.h"
// <<<<<<<< Begin include from /org/esa/beam/extapi/gen/c/CModuleGenerator-stub-types.h
typedef char byte;
typedef unsigned char boolean;
typedef long long dlong;

typedef void* Object;
typedef void* String;
// >>>>>>>> End include from /org/esa/beam/extapi/gen/c/CModuleGenerator-stub-types.h
// PyAPI_DATA(PyTypeObject) Shape_Type;
extern PyTypeObject Shape_Type;
// PyAPI_DATA(PyTypeObject) MapTransform_Type;
extern PyTypeObject MapTransform_Type;
// PyAPI_DATA(PyTypeObject) ImageGeometry_Type;
extern PyTypeObject ImageGeometry_Type;
// PyAPI_DATA(PyTypeObject) Parser_Type;
extern PyTypeObject Parser_Type;
// PyAPI_DATA(PyTypeObject) GeoCoding_Type;
extern PyTypeObject GeoCoding_Type;
// PyAPI_DATA(PyTypeObject) ProductData_Type;
extern PyTypeObject ProductData_Type;
// PyAPI_DATA(PyTypeObject) AffineTransform_Type;
extern PyTypeObject AffineTransform_Type;
// PyAPI_DATA(PyTypeObject) Mask_Type;
extern PyTypeObject Mask_Type;
// PyAPI_DATA(PyTypeObject) Double_Type;
extern PyTypeObject Double_Type;
// PyAPI_DATA(PyTypeObject) GPF_Type;
extern PyTypeObject GPF_Type;
// PyAPI_DATA(PyTypeObject) IndexCoding_Type;
extern PyTypeObject IndexCoding_Type;
// PyAPI_DATA(PyTypeObject) Term_Type;
extern PyTypeObject Term_Type;
// PyAPI_DATA(PyTypeObject) RasterDataNode_Type;
extern PyTypeObject RasterDataNode_Type;
// PyAPI_DATA(PyTypeObject) Class_Type;
extern PyTypeObject Class_Type;
// PyAPI_DATA(PyTypeObject) Product_AutoGrouping_Type;
extern PyTypeObject Product_AutoGrouping_Type;
// PyAPI_DATA(PyTypeObject) PixelPos_Type;
extern PyTypeObject PixelPos_Type;
// PyAPI_DATA(PyTypeObject) BitRaster_Type;
extern PyTypeObject BitRaster_Type;
// PyAPI_DATA(PyTypeObject) ImageOutputStream_Type;
extern PyTypeObject ImageOutputStream_Type;
// PyAPI_DATA(PyTypeObject) Stx_Type;
extern PyTypeObject Stx_Type;
// PyAPI_DATA(PyTypeObject) Rectangle_Type;
extern PyTypeObject Rectangle_Type;
// PyAPI_DATA(PyTypeObject) Dimension_Type;
extern PyTypeObject Dimension_Type;
// PyAPI_DATA(PyTypeObject) ProductIO_Type;
extern PyTypeObject ProductIO_Type;
// PyAPI_DATA(PyTypeObject) ProductNode_Type;
extern PyTypeObject ProductNode_Type;
// PyAPI_DATA(PyTypeObject) AngularDirection_Type;
extern PyTypeObject AngularDirection_Type;
// PyAPI_DATA(PyTypeObject) SimpleFeatureType_Type;
extern PyTypeObject SimpleFeatureType_Type;
// PyAPI_DATA(PyTypeObject) SampleCoding_Type;
extern PyTypeObject SampleCoding_Type;
// PyAPI_DATA(PyTypeObject) Object_Type;
extern PyTypeObject Object_Type;
// PyAPI_DATA(PyTypeObject) ProductReader_Type;
extern PyTypeObject ProductReader_Type;
// PyAPI_DATA(PyTypeObject) ProductReaderPlugIn_Type;
extern PyTypeObject ProductReaderPlugIn_Type;
// PyAPI_DATA(PyTypeObject) Integer_Type;
extern PyTypeObject Integer_Type;
// PyAPI_DATA(PyTypeObject) ProductData_UTC_Type;
extern PyTypeObject ProductData_UTC_Type;
// PyAPI_DATA(PyTypeObject) Band_Type;
extern PyTypeObject Band_Type;
// PyAPI_DATA(PyTypeObject) ColorPaletteDef_Point_Type;
extern PyTypeObject ColorPaletteDef_Point_Type;
// PyAPI_DATA(PyTypeObject) RenderedImage_Type;
extern PyTypeObject RenderedImage_Type;
// PyAPI_DATA(PyTypeObject) Placemark_Type;
extern PyTypeObject Placemark_Type;
// PyAPI_DATA(PyTypeObject) IndexValidator_Type;
extern PyTypeObject IndexValidator_Type;
// PyAPI_DATA(PyTypeObject) Area_Type;
extern PyTypeObject Area_Type;
// PyAPI_DATA(PyTypeObject) ComponentColorModel_Type;
extern PyTypeObject ComponentColorModel_Type;
// PyAPI_DATA(PyTypeObject) Iterator_Type;
extern PyTypeObject Iterator_Type;
// PyAPI_DATA(PyTypeObject) MathTransform_Type;
extern PyTypeObject MathTransform_Type;
// PyAPI_DATA(PyTypeObject) CoordinateReferenceSystem_Type;
extern PyTypeObject CoordinateReferenceSystem_Type;
// PyAPI_DATA(PyTypeObject) ProductWriterPlugIn_Type;
extern PyTypeObject ProductWriterPlugIn_Type;
// PyAPI_DATA(PyTypeObject) Rectangle2D_Type;
extern PyTypeObject Rectangle2D_Type;
// PyAPI_DATA(PyTypeObject) File_Type;
extern PyTypeObject File_Type;
// PyAPI_DATA(PyTypeObject) GeoPos_Type;
extern PyTypeObject GeoPos_Type;
// PyAPI_DATA(PyTypeObject) ProductNodeGroup_Type;
extern PyTypeObject ProductNodeGroup_Type;
// PyAPI_DATA(PyTypeObject) MapProjection_Type;
extern PyTypeObject MapProjection_Type;
// PyAPI_DATA(PyTypeObject) ProductManager_Type;
extern PyTypeObject ProductManager_Type;
// PyAPI_DATA(PyTypeObject) FlagCoding_Type;
extern PyTypeObject FlagCoding_Type;
// PyAPI_DATA(PyTypeObject) IndexColorModel_Type;
extern PyTypeObject IndexColorModel_Type;
// PyAPI_DATA(PyTypeObject) Operator_Type;
extern PyTypeObject Operator_Type;
// PyAPI_DATA(PyTypeObject) OperatorSpiRegistry_Type;
extern PyTypeObject OperatorSpiRegistry_Type;
// PyAPI_DATA(PyTypeObject) ImageInfo_HistogramMatching_Type;
extern PyTypeObject ImageInfo_HistogramMatching_Type;
// PyAPI_DATA(PyTypeObject) BitmaskDef_Type;
extern PyTypeObject BitmaskDef_Type;
// PyAPI_DATA(PyTypeObject) ProductNodeListener_Type;
extern PyTypeObject ProductNodeListener_Type;
// PyAPI_DATA(PyTypeObject) ProductUtils_Type;
extern PyTypeObject ProductUtils_Type;
// PyAPI_DATA(PyTypeObject) Map_Type;
extern PyTypeObject Map_Type;
// PyAPI_DATA(PyTypeObject) MetadataElement_Type;
extern PyTypeObject MetadataElement_Type;
// PyAPI_DATA(PyTypeObject) Datum_Type;
extern PyTypeObject Datum_Type;
// PyAPI_DATA(PyTypeObject) Pointing_Type;
extern PyTypeObject Pointing_Type;
// PyAPI_DATA(PyTypeObject) Color_Type;
extern PyTypeObject Color_Type;
// PyAPI_DATA(PyTypeObject) PlacemarkDescriptor_Type;
extern PyTypeObject PlacemarkDescriptor_Type;
// PyAPI_DATA(PyTypeObject) PointingFactory_Type;
extern PyTypeObject PointingFactory_Type;
// PyAPI_DATA(PyTypeObject) TransectProfileData_Type;
extern PyTypeObject TransectProfileData_Type;
// PyAPI_DATA(PyTypeObject) PlacemarkGroup_Type;
extern PyTypeObject PlacemarkGroup_Type;
// PyAPI_DATA(PyTypeObject) Product_Type;
extern PyTypeObject Product_Type;
// PyAPI_DATA(PyTypeObject) Point2D_Type;
extern PyTypeObject Point2D_Type;
// PyAPI_DATA(PyTypeObject) ProductVisitor_Type;
extern PyTypeObject ProductVisitor_Type;
// PyAPI_DATA(PyTypeObject) Scaling_Type;
extern PyTypeObject Scaling_Type;
// PyAPI_DATA(PyTypeObject) WritableNamespace_Type;
extern PyTypeObject WritableNamespace_Type;
// PyAPI_DATA(PyTypeObject) Set_Type;
extern PyTypeObject Set_Type;
// PyAPI_DATA(PyTypeObject) MultiLevelImage_Type;
extern PyTypeObject MultiLevelImage_Type;
// PyAPI_DATA(PyTypeObject) ROI_Type;
extern PyTypeObject ROI_Type;
// PyAPI_DATA(PyTypeObject) RenderingHints_Key_Type;
extern PyTypeObject RenderingHints_Key_Type;
// PyAPI_DATA(PyTypeObject) Collection_Type;
extern PyTypeObject Collection_Type;
// PyAPI_DATA(PyTypeObject) ProductManager_Listener_Type;
extern PyTypeObject ProductManager_Listener_Type;
// PyAPI_DATA(PyTypeObject) GeoTIFFMetadata_Type;
extern PyTypeObject GeoTIFFMetadata_Type;
// PyAPI_DATA(PyTypeObject) ColorPaletteDef_Type;
extern PyTypeObject ColorPaletteDef_Type;
// PyAPI_DATA(PyTypeObject) MapInfo_Type;
extern PyTypeObject MapInfo_Type;
// PyAPI_DATA(PyTypeObject) ImageInfo_Type;
extern PyTypeObject ImageInfo_Type;
// PyAPI_DATA(PyTypeObject) Histogram_Type;
extern PyTypeObject Histogram_Type;
// PyAPI_DATA(PyTypeObject) String_Type;
extern PyTypeObject String_Type;
// PyAPI_DATA(PyTypeObject) BufferedImage_Type;
extern PyTypeObject BufferedImage_Type;
// PyAPI_DATA(PyTypeObject) RGBChannelDef_Type;
extern PyTypeObject RGBChannelDef_Type;
// PyAPI_DATA(PyTypeObject) TiePointGrid_Type;
extern PyTypeObject TiePointGrid_Type;
// PyAPI_DATA(PyTypeObject) SimpleFeature_Type;
extern PyTypeObject SimpleFeature_Type;
// PyAPI_DATA(PyTypeObject) ProductSubsetDef_Type;
extern PyTypeObject ProductSubsetDef_Type;
// PyAPI_DATA(PyTypeObject) ProductWriter_Type;
extern PyTypeObject ProductWriter_Type;
// PyAPI_DATA(PyTypeObject) MetadataAttribute_Type;
extern PyTypeObject MetadataAttribute_Type;
// PyAPI_DATA(PyTypeObject) ProgressMonitor_Type;
extern PyTypeObject ProgressMonitor_Type;
// PyAPI_DATA(PyTypeObject) VectorDataNode_Type;
extern PyTypeObject VectorDataNode_Type;
// PyAPI_DATA(PyTypeObject) GeneralPath_Type;
extern PyTypeObject GeneralPath_Type;
// PyAPI_DATA(PyTypeObject) ImageInputStream_Type;
extern PyTypeObject ImageInputStream_Type;
// PyAPI_DATA(PyTypeObject) RenderingHints_Type;
extern PyTypeObject RenderingHints_Type;

#ifdef __cplusplus
} /* extern "C" */
#endif
#endif /* !BEAMPY_H */