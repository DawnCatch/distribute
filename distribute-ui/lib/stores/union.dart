import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "union.g.dart";

@riverpod
class UnionState extends _$UnionState {

  @override
  Future<Union?> build() async {
    dynamic response = await Http.get("/relation/list/union");
    Result<Union> unionResult = Result.fromJson(response, Union.fromJson);
    if (unionResult.status == true && unionResult.data != null) {
      return unionResult.data!;
    } return null;
  }
}