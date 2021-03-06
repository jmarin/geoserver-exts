package org.opengeo.data.importer.transform;

import org.geotools.data.DataStore;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengeo.data.importer.ImportItem;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Attribute that maps an attribute from one type to another.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class AttributeRemapTransform extends AbstractVectorTransform implements InlineVectorTransform {
    
    private static final long serialVersionUID = 1L;

    /** field to remap */
    protected String field;

    /** type to remap to */
    protected Class type;
    
    public AttributeRemapTransform(String field, Class type) {
        this.field = field;
        this.type = type;
    }
    
    protected AttributeRemapTransform() {
        
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public SimpleFeatureType apply(ImportItem item, DataStore dataStore,
            SimpleFeatureType featureType) throws Exception {
        //remap the type
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(featureType);

        int index = featureType.indexOf(field);
        if (index < 0) {
            throw new Exception("FeatureType " + featureType.getName() + " does not have attribute named '" + field + "'");
        }
        
        //remap the attribute to type date and ensure schema ordering is the same
        //@todo improve FeatureTypeBuilder to support this directly
        AttributeDescriptor existing = builder.remove(field);
        AttributeTypeBuilder attBuilder = new AttributeTypeBuilder();
        attBuilder.init(existing);
        attBuilder.setBinding(type);
        builder.add(index, attBuilder.buildDescriptor(field));

        return builder.buildFeatureType();
    }

    public SimpleFeature apply(ImportItem item, DataStore dataStore, SimpleFeature oldFeature, 
        SimpleFeature feature) throws Exception {
        return feature;
    }

}
