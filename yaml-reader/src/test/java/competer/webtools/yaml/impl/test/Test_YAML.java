package competer.webtools.yaml.impl.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.*;
import competer.webtools.yaml.api.IYAMLArray;
import competer.webtools.yaml.api.IYAMLArrayElementProcessor;
import competer.webtools.yaml.api.IYAMLArtifact;
import competer.webtools.yaml.api.IYAMLObject;
import competer.webtools.yaml.impl.YAMLFactory;

public class Test_YAML
{
    private interface IInputStreamProcessor
    {
        void process( InputStream _stream ) throws Exception;
    }

    private void stream( String _resourceName, IInputStreamProcessor _processor ) throws Exception
    {
        InputStream stream = getClass().getResourceAsStream( _resourceName );
        if ( stream == null )
            throw new Exception( String.format( "Resource \"%s\" couldn't be opened for the test.",
                _resourceName ) );
        try
        {
            _processor.process( stream );
        }
        finally
        {
            stream.close();
        }
    }

    @Test
    public void Can_load_yaml() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
                assertNotNull( yo );
            }
        } );
    }

    @Test
    public void Can_read_single_properties_of_all_types() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
                assertNull( yo.ob( "nothing" ) );
                assertEquals( "string1", yo.str( "str" ) );
                assertEquals( 85, yo.longNum( "long" ) );
                assertEquals( true, yo.bool( "bool" ) );
                assertTrue( yo.ar( "ar" ) instanceof IYAMLArray );
                assertEquals( "string2", yo.ar( "ar" ).str( 0 ) );
                assertEquals( 86, yo.ar( "ar" ).longNum( 1 ) );
                assertEquals( false, yo.ar( "ar" ).bool( 2 ) );
                assertEquals( "string3", yo.ar( "ar" ).ob( 3 ).str( "str" ) );
                assertEquals( 87, yo.ar( "ar" ).ob( 3 ).longNum( "long" ) );
            }
        } );
    }

    @Test
    public void Can_navigate_back_and_forth() throws Exception
    {
        stream( "resources/propertyBrowsing.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
//                contact:
//                    lastName: Laman
//                    firstName: Peter
//                    parents:
//                      father:
//                        lastName: Laman
//                        firstName: Paul
//                        father:
//                          lastName: Laman
//                          firstName: Jan
//                      mother:
//                        lastName: Spierenburg
//                        firstName: Gré
//                  otherFamilyMembers:
//                  - lastName: van Wijk
//                    firstName: Nelly
//                    parents:
//                      father:
//                        lastName: van Wijk
//                        firstName: Ad
//                      mother:
//                        lastName: Sleutel
//                        firstName: Ellen
//                  - lastName: Laman
//                    firstName: Joëlle
//                  - lastName: Laman
//                    firstName: Manuel
                assertEquals( "Jan", yo.str( "contact", "parents", "father", "father", "firstName" ) );
                assertEquals( "Gré", yo.str( "contact", "parents", "father", "father", "firstName", "..", "..", "..", "mother", ".", "firstName" ) );
            }
        } );
    }

    @Test
    public void Can_read_deep_properties() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
                assertEquals( 87L, yo.longNum( "ar", 3, "long" ) );
            }
        } );
    }

    @Test
    public void Paths_are_rendered_ok() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
                assertEquals( "", yo.path() );
                assertEquals( "ar", yo.ar( "ar" ).path() );
                assertEquals( "ar/3", yo.ob( "ar", 3 ).path() );
            }
        } );
    }

    @Test
    public void Can_navigate_through_yaml() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );

                // Obtain deep object
                IYAMLObject obj = yo.ob( "ar", 3 );
                assertNotNull( obj );

                // Navigate from there back to the array and pick element 1
                assertEquals( 86, obj.longNum( "..", 1 ) );
            }
        } );
    }

    @Test
    public void Can_iterator_array_with_for_each() throws Exception
    {
        stream( "resources/fibonacci.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLArray yo = YAMLFactory.loadArray( _stream );

                final ArrayList<Integer> fibo = new ArrayList<Integer>();
                yo.forEach( Integer.class, new IYAMLArrayElementProcessor<Integer>()
                {
                    @Override
                    public boolean process( int _index, Integer _element )
                    {
                        fibo.add( _element );
                        return true;
                    }
                } );
                assertEquals( yo.size(), fibo.size() );
                int pred1 = 0;
                int pred2 = 1;
                for ( int i = 0; i < fibo.size(); i++ )
                {
                    int newValue = pred1 + pred2;
                    assertEquals( newValue, fibo.get( i ).intValue() );
                    pred1 = pred2;
                    pred2 = newValue;
                }
            }
        } );
    }

    @Test
    public void Can_browse_property_name() throws Exception
    {
        stream( "resources/propertyBrowsing.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLArtifact yo = YAMLFactory.load( _stream );
                Iterator<IYAMLArtifact> it = yo.propertyBrowser( "firstName" );

                // Collect all of the names
                ArrayList<String> namesList = new ArrayList<String>();
                while ( it.hasNext() )
                {
                    IYAMLArtifact art = it.next();
                    StringBuilder sb = new StringBuilder();
                    sb.append( art.key().toString() );
                    sb.append( ": " );
                    sb.append( art.str( "firstName" ) );
                    sb.append( ' ' );
                    sb.append( art.str( "lastName" ) );
                    namesList.add( sb.toString() );
                }

                // Now create an array of names
                String[] names = namesList.toArray( new String[namesList.size()] );
                assertArrayEquals(
                    new String[]
                    {
                        "contact: Peter Laman",
                        "father: Paul Laman",
                        "father: Jan Laman",
                        "mother: Gré Spierenburg",
                        "0: Nelly van Wijk",
                        "father: Ad van Wijk",
                        "mother: Ellen Sleutel",
                        "1: Joëlle Laman",
                        "2: Manuel Laman"
                    },
                    names );
            }
        } );
    }

    @Test
    public void Can_apply_default_for_optional_property() throws Exception
    {
        stream( "resources/allTypes.yaml", new IInputStreamProcessor()
        {
            @Override
            public void process( InputStream _stream ) throws Exception
            {
                IYAMLObject yo = YAMLFactory.loadObject( _stream );
                assertEquals( "default Value", yo.ostrDefault( "default Value", "noSuchValue" ) );
            }
        } );
    }
}
