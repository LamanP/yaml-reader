package competer.webtools.yaml.api;

public interface IYAMLArrayElementProcessor<T>
{
    boolean process( int _index, T _element );
}
