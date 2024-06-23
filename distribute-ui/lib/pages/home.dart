import 'dart:convert';

import 'package:distribute/common/http.dart';
import 'package:distribute/components/home/home_body.dart';
import 'package:distribute/components/home/home_drawer.dart';
import 'package:distribute/models/index.dart';
import 'package:distribute/stores/message.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer(
      builder: (context, ref, child) {
        var stream = Http.socket("/chat");
        final messageNotifier = ref.read(messageStateProvider.notifier);
        final messageState = ref.watch(messageStateProvider);
        messageState.when(
            data: (data) {
              stream.forEach((e) {
                messageNotifier.add(data, Message.fromJson(jsonDecode(e)));
              });
            },
            error: (error, stack) {},
            loading: () {});
        return child!;
      },
      child: Scaffold(
        drawer: const HomeDrawer(),
        floatingActionButton: FloatingActionButton(
            onPressed: () {}, child: const Icon(Icons.add)),
        body: const HomeBody(),
      ),
    );
  }
}
