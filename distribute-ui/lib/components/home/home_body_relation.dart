import 'package:distribute/components/home/home_body_relation_item.dart';
import 'package:distribute/stores/union.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:provider/provider.dart';

class HomeBodyRelation extends ConsumerStatefulWidget {
  const HomeBodyRelation({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() =>
      _HomeBodyRelationState();
}

class _HomeBodyRelationState extends ConsumerState<HomeBodyRelation> {
  @override
  Widget build(BuildContext context) {
    final paths = ref.watch(unionStateProvider.select((it) => it.value?.paths));
    return TabBarView(
      children: [buildFriendTab(), buildGroupTab()] +
          (paths?.map((it) => buildPathTab(it)).toList() ?? []),
    );
  }

  Widget buildFriendTab() {
    final friends =
    ref.watch(unionStateProvider.select((it) => it.value?.friends));
    return Column(
      children: friends?.map((it) {
        return HomeBodyRelationItem(type: it.type, id: it.id);
      }).toList() ??
          [const CircularProgressIndicator()],
    );
  }

  Widget buildGroupTab() {
    final groups =
    ref.watch(unionStateProvider.select((it) => it.value?.groups));
    return Column(
      children: groups?.map((it) {
        return HomeBodyRelationItem(type: it.type, id: it.id);
      }).toList() ??
          [const CircularProgressIndicator()],
    );
  }

  Widget buildPathTab(String path) {
    final relations =
    ref.watch(unionStateProvider.select((it) => it.value?.relations));
    return Column(
      children: relations?.where((it) => it.path == path).map((it) {
        return HomeBodyRelationItem(type: it.type, id: it.id);
      }).toList() ??
          [const CircularProgressIndicator()],
    );
  }
}
