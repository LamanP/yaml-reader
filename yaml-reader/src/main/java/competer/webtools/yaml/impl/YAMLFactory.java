package competer.webtools.yaml.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import competer.webtools.yaml.api.IYAMLArray;
import competer.webtools.yaml.api.IYAMLArtifact;
import competer.webtools.yaml.api.IYAMLObject;

public abstract class YAMLFactory
{
    public static IYAMLObject loadObject( InputStream _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    public static IYAMLArray loadArray( InputStream _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    public static IYAMLArtifact load( InputStream _in )
    {
        Yaml yaml = new Yaml();
        Object content = yaml.load( _in );
        if ( content instanceof Map<?, ?> )
            return new YObject( null, null, (Map<?, ?>)content );
        else
            return new YArray( null, null, (List<?>)content );
    }

    public static IYAMLObject loadObject( String _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    public static IYAMLArray loadArray( String _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    public static IYAMLArtifact load( String _in )
    {
        Yaml yaml = new Yaml();
        Object content = yaml.load( _in );
        if ( content instanceof Map<?, ?> )
            return new YObject( null, null, (Map<?, ?>)content );
        else
            return new YArray( null, null, (List<?>)content );
    }

    public static IYAMLObject loadObject( Reader _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    public static IYAMLArray loadArray( Reader _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    public static IYAMLArtifact load( Reader _in )
    {
        Yaml yaml = new Yaml();
        Object content = yaml.load( _in );
        if ( content instanceof Map<?, ?> )
            return new YObject( null, null, (Map<?, ?>)content );
        else
            return new YArray( null, null, (List<?>)content );
    }
}
