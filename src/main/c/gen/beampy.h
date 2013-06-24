#ifndef BEAMPY_H
#define BEAMPY_H

#ifdef __cplusplus
extern "C" {
#endif

#include <Python.h>
#include <structmember.h>

extern PyTypeObject Shape_Type;
extern PyTypeObject MapTransform_Type;
extern PyTypeObject ImageGeometry_Type;
extern PyTypeObject Parser_Type;
extern PyTypeObject GeoCoding_Type;
extern PyTypeObject ProductData_Type;
extern PyTypeObject OperatorSpi_Type;
extern PyTypeObject AffineTransform_Type;
extern PyTypeObject Mask_Type;
extern PyTypeObject Double_Type;
extern PyTypeObject GPF_Type;
extern PyTypeObject IndexCoding_Type;
extern PyTypeObject Term_Type;
extern PyTypeObject RasterDataNode_Type;
extern PyTypeObject Class_Type;
extern PyTypeObject ServiceRegistry_Type;
extern PyTypeObject Product_AutoGrouping_Type;
extern PyTypeObject PixelPos_Type;
extern PyTypeObject BitRaster_Type;
extern PyTypeObject ImageOutputStream_Type;
extern PyTypeObject Stx_Type;
extern PyTypeObject Rectangle_Type;
extern PyTypeObject Dimension_Type;
extern PyTypeObject ProductIO_Type;
extern PyTypeObject ProductNode_Type;
extern PyTypeObject AngularDirection_Type;
extern PyTypeObject SimpleFeatureType_Type;
extern PyTypeObject SampleCoding_Type;
extern PyTypeObject Object_Type;
extern PyTypeObject ProductReader_Type;
extern PyTypeObject ProductReaderPlugIn_Type;
extern PyTypeObject Integer_Type;
extern PyTypeObject ProductData_UTC_Type;
extern PyTypeObject Band_Type;
extern PyTypeObject ColorPaletteDef_Point_Type;
extern PyTypeObject RenderedImage_Type;
extern PyTypeObject Placemark_Type;
extern PyTypeObject IndexValidator_Type;
extern PyTypeObject Area_Type;
extern PyTypeObject ComponentColorModel_Type;
extern PyTypeObject Iterator_Type;
extern PyTypeObject MathTransform_Type;
extern PyTypeObject CoordinateReferenceSystem_Type;
extern PyTypeObject ProductWriterPlugIn_Type;
extern PyTypeObject File_Type;
extern PyTypeObject GeoPos_Type;
extern PyTypeObject ProductNodeGroup_Type;
extern PyTypeObject MapProjection_Type;
extern PyTypeObject ProductManager_Type;
extern PyTypeObject FlagCoding_Type;
extern PyTypeObject IndexColorModel_Type;
extern PyTypeObject OperatorSpiRegistry_Type;
extern PyTypeObject Operator_Type;
extern PyTypeObject ImageInfo_HistogramMatching_Type;
extern PyTypeObject BitmaskDef_Type;
extern PyTypeObject ProductNodeListener_Type;
extern PyTypeObject Map_Type;
extern PyTypeObject MetadataElement_Type;
extern PyTypeObject ProductUtils_Type;
extern PyTypeObject Datum_Type;
extern PyTypeObject Pointing_Type;
extern PyTypeObject Color_Type;
extern PyTypeObject PlacemarkDescriptor_Type;
extern PyTypeObject PointingFactory_Type;
extern PyTypeObject TransectProfileData_Type;
extern PyTypeObject PlacemarkGroup_Type;
extern PyTypeObject Product_Type;
extern PyTypeObject Point2D_Type;
extern PyTypeObject ProductVisitor_Type;
extern PyTypeObject Scaling_Type;
extern PyTypeObject WritableNamespace_Type;
extern PyTypeObject Set_Type;
extern PyTypeObject MultiLevelImage_Type;
extern PyTypeObject ROI_Type;
extern PyTypeObject RenderingHints_Key_Type;
extern PyTypeObject Collection_Type;
extern PyTypeObject ProductManager_Listener_Type;
extern PyTypeObject GeoTIFFMetadata_Type;
extern PyTypeObject ColorPaletteDef_Type;
extern PyTypeObject MapInfo_Type;
extern PyTypeObject ImageInfo_Type;
extern PyTypeObject Histogram_Type;
extern PyTypeObject String_Type;
extern PyTypeObject BufferedImage_Type;
extern PyTypeObject RGBChannelDef_Type;
extern PyTypeObject TiePointGrid_Type;
extern PyTypeObject SimpleFeature_Type;
extern PyTypeObject ProductSubsetDef_Type;
extern PyTypeObject ProductWriter_Type;
extern PyTypeObject MetadataAttribute_Type;
extern PyTypeObject ProgressMonitor_Type;
extern PyTypeObject VectorDataNode_Type;
extern PyTypeObject GeneralPath_Type;
extern PyTypeObject ImageInputStream_Type;
extern PyTypeObject RenderingHints_Type;

#ifdef __cplusplus
} /* extern "C" */
#endif
#endif /* !BEAMPY_H */