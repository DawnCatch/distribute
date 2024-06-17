import 'package:animations/animations.dart';
import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/components/home/home_body.dart';
import 'package:distribute/components/home/home_drawer.dart';
import 'package:distribute/domains/union.dart';
import 'package:distribute/models/union.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with TickerProviderStateMixin {
  late UnionChangeNotifier unionChangeNotifier;

  @override
  void initState() {
    super.initState();
  }

  Future<void> initData() async {
    dynamic unionResponse = await Http.get("/relation/list/union");
    dynamic historyResponse =
        await Http.post("/message/history", {"from": "", "to": ""});
    Result<Union> unionResult = Result.fromJson(unionResponse, (json) {
      return Union.fromJson(json);
    });
    if (unionResult.status == true && unionResult.data != null) {
      Union data = unionResult.data!;
      unionChangeNotifier.follows = data.follows;
      unionChangeNotifier.fans = data.fans.map((item) => item as int).toList();
      unionChangeNotifier.groups = data.groups;
      unionChangeNotifier.applications = data.applications;
    }
  }

  @override
  Widget build(BuildContext context) {
    unionChangeNotifier = Provider.of<UnionChangeNotifier>(context);
    initData();
    return Scaffold(
      drawer: const HomeDrawer(),
      floatingActionButton:
          FloatingActionButton(onPressed: _onAdd, child: const Icon(Icons.add)),
      body: const HomeBody(),
    );
  }

  void _onAdd() {}
}