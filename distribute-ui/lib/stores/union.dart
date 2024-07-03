import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "union.g.dart";

@riverpod
class UnionState extends _$UnionState {
  static Union union = Union();

  @override
  Future<Union> build() async {
    dynamic response = await Http.get("/relation/list/union");
    Result<Union> unionResult = Result.fromJson(response, Union.fromJson);
    if (unionResult.status == true && unionResult.data != null) {
      union = unionResult.data!;
    }
    return union;
  }

  void set() {
    state = AsyncValue.data(union);
  }

  Future<void> addFollows(num id) async {
    Relation relation = Relation()
      ..type = false
      ..id = id
      ..title = ""
      ..path = "/";
    int index = union.follows.indexWhere((it) => it.id == id);
    if (index == -1) {
      union.follows.add(relation);
    }
    set();
  }

  Future<void> removeFollows(num id) async {
    union.follows.removeWhere((it) => it.id == id);
    set();
  }
}
