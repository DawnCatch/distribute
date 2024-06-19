import 'package:flutter/material.dart';

class HomeBodySearch extends StatefulWidget {
  const HomeBodySearch({super.key});

  @override
  State<StatefulWidget> createState() => _HomeBodySearchState();
}

class _HomeBodySearchState extends State<HomeBodySearch> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return const TabBarView(
      children: [
        Center(child: Text('10')),
        Center(child: Text('11')),
        Center(child: Text('12')),
      ],
    );
  }
}
