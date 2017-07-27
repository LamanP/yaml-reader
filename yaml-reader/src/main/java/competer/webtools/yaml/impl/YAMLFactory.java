package competer.webtools.yaml.impl;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import competer.webtools.yaml.api.IYAMLArray;
import competer.webtools.yaml.api.IYAMLArtifact;
import competer.webtools.yaml.api.IYAMLObject;

/**
 * This is your starting point. Parse your YAML data here and get a convenient interface
 * to explore it.
 */
public abstract class YAMLFactory
{
    /**
     * Load YAML object resource from an input stream.
     *
     * @param _in Inputstream
     * @return An interface to explore the YAML object
     */
    public static IYAMLObject loadObject( InputStream _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    /**
     * Load YAML array resource from an input stream
     *
     * @param _in Inputstream
     * @return An interface to explore the YAML array
     */
    public static IYAMLArray loadArray( InputStream _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    /**
     * Load a YAML artifact resource from an input stream. The artifact may be either an object
     * or an array.
     *
     * @param _in Inputstream
     * @return An interface to explore the YAML artifact
     */
    public static IYAMLArtifact load( InputStream _in )
    {
        Yaml yaml = new Yaml();
        Object content = yaml.load( _in );
        if ( content instanceof Map<?, ?> )
            return new YObject( null, null, (Map<?, ?>)content );
        else
            return new YArray( null, null, (List<?>)content );
    }

    /**
     * Load a YAML object resource from a string.
     *
     * @param _in String that must be parsed
     * @return An interface to explore the YAML object
     */
    public static IYAMLObject loadObject( String _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    /**
     * Load a YAML array resource from a string.
     *
     * @param _in String that must be parsed
     * @return An interface to explore the YAML object
     */
    public static IYAMLArray loadArray( String _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    /**
     * Load a YAML artifact resource from a string. The artifact may be either an object
     * or an array.
     *
     * @param _in String that must be parsed
     * @return An interface to explore the YAML object
     */
    public static IYAMLArtifact load( String _in )
    {
        Yaml yaml = new Yaml();
        Object content = yaml.load( _in );
        if ( content instanceof Map<?, ?> )
            return new YObject( null, null, (Map<?, ?>)content );
        else
            return new YArray( null, null, (List<?>)content );
    }

    /**
     * Load a YAML object resource from a Reader.
     *
     * @param _in Reader that must be parsed
     * @return An interface to explore the YAML object
     */
    public static IYAMLObject loadObject( Reader _in )
    {
        Yaml yaml = new Yaml();
        return new YObject( null, null, (Map<?, ?>)yaml.load( _in ) );
    }

    /**
     * Load a YAML array resource from a Reader.
     *
     * @param _in Reader that must be parsed
     * @return An interface to explore the YAML array
     */
    public static IYAMLArray loadArray( Reader _in )
    {
        Yaml yaml = new Yaml();
        return new YArray( null, null, (List<?>)yaml.load( _in ) );
    }

    /**
     * Load a YAML artifact resource from a reader. The artifact may be either an object
     * or an array.
     *
     * @param _in Reader that must be parsed
     * @return An interface to explore the YAML object
     */
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
