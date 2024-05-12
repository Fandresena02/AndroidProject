package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new fr.android.devmobproject.DataBinderMapperImpl());
  }
}
