package competer.webtools.yaml.api;

public class YAMLException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public YAMLException(
        String _messageFormat, String _path, IYAMLAddressResolver _addressResolver, Throwable _cause,
        Object... _messageArgs )
    {
        super( buildMessage( _messageFormat, _addressResolver, _messageArgs ), _cause );
    }

    private static String buildMessage(
        String _messageFormat , IYAMLAddressResolver _addressResolver , Object... _messageArgs  )
    {
        return String.format( "YAML extraction error for path \"%s\": %s",
            _addressResolver.fullPath(), String.format( _messageFormat, _messageArgs ) );
    }
}
