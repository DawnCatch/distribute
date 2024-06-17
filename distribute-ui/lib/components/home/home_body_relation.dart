import 'package:flutter/material.dart';

class HomeBodyRelation extends StatefulWidget {
  const HomeBodyRelation({super.key});

  @override
  State<StatefulWidget> createState() => _HomeBodyRelationState();
}

class _HomeBodyRelationState extends State<HomeBodyRelation> {
  @override

  Widget build(BuildContext context) {
    return const TabBarView(
      children: [
        Center(child: Text('00')),
        Center(child: Text('01')),
        Center(child: Text('02')),
      ],
    );
  }
}