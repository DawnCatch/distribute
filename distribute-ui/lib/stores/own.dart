import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/models/index.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part "own.g.dart";

@riverpod
class OwnState extends _$OwnState {
  @override
  Profile? build() => null;

  void set(Profile? profile) {
    if (profile == null) return;
    state = profile;
  }
}
