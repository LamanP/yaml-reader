package competer.webtools.yaml.api;

import java.util.Iterator;

/**
 * The IYAMLArtifact interface represents a YAML artifact, that can be either an object or an array.
 */
public interface IYAMLArtifact
{
    /**
     * @return Parent artifact, or null if the artifact is the top artifact in the parsed YAML resource.
     */
    IYAMLArtifact parent();

    /**
     * path() returns a string representation of the property path that leads to this artifact
     * from the root of the YAML resource.
     *
     * @return Path representation.
     */
    String path();

    /**
     * Returns a required array at the specified address.
     *
     * @param _address Address of the array within this artifact.
     * @return
     * @throws YAMLException
     */
    IYAMLArray ar( Object... _address );
    IYAMLArray oar( Object... _address );
    IYAMLObject ob( Object... _address );
    IYAMLObject oob( Object... _address );
    String str( Object... _address );
    String ostr( Object... _address );
    String ostrDefault( String _defaultValue, Object... _address );
    long longNum( Object... _address );
    Long olongNum( Object... _address );
    Long olongNumDefault( Long _defaultValue, Object... _address );
    int intNum( Object... _address );
    Integer ointNum( Object... _address );
    Integer ointNumDefault( Integer _defaultValue, Object... _address );
    boolean bool( Object... _address );
    Boolean obool( Object... _address );
    Boolean oboolDefault( Boolean _defaultValue, Object... _address );
    <T> T get( Class<T> _class , boolean _required , Object... _address  );
    Object getRaw( Object _key );
    boolean has( Object _key );
    boolean isNull( Object _key );
    void require( Object _key );
    Object key();
    Object key( Object... _address );

    /**
     * Produces an iterator that iterates all artifacts that contain the specified address
     *
     * @param _address
     * @return
     */
    Iterator<IYAMLArtifact> propertyBrowser( Object _address );
    Iterator<?> addressIterator();
    /**
     * @param _address
     * @return
     */
}
