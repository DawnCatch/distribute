import 'package:distribute/domains/union.dart';
import 'package:distribute/domains/profile.dart';
import 'package:distribute/router/index.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'common/global.dart';

void main() {
  Global.init().then((e) => runApp(const App()));
}

class App extends StatefulWidget {
  const App({super.key});

  @override
  State<StatefulWidget> createState() => AppState();
}

class AppState extends State<App> with WidgetsBindingObserver {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    if (state == AppLifecycleState.paused) {
      Global.save();
    }
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => ProfileChangeNotifier()),
          ChangeNotifierProvider(create: (_) => UnionChangeNotifier())
        ],
        child: MaterialApp(
          title: 'Flutter Demo',
          initialRoute: "sign",
          theme: ThemeData(
            colorScheme: ColorScheme.fromSeed(
                seedColor: Colors.blue, brightness: Brightness.dark),
            useMaterial3: true,
          ),
          debugShowCheckedModeBanner: false,
          routes: routes,
        ));
  }
}
