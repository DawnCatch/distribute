import 'package:distribute/components/home/home_drawer.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

class TestPage extends StatefulWidget {
  const TestPage({super.key});

  @override
  State<StatefulWidget> createState() => TestPageState();
}

class TestPageState extends State<TestPage> with TickerProviderStateMixin {
  late TabController _tabController;

  late List<int> _tabs;

  @override
  void initState() {
    _tabController = TabController(length: 2, vsync: this);
    _tabs = [];
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: const HomeDrawer(),
      body: Container(
        color: Theme.of(context).colorScheme.surfaceBright,
        child: SafeArea(
          child: DefaultTabController(
            length: _tabs.length,
            child: NestedScrollView(
              headerSliverBuilder: (context, isScrolled) {
                return [
                  SliverAppBar(
                    surfaceTintColor: Theme.of(context).colorScheme.onSecondary,
                    foregroundColor:
                        Theme.of(context).colorScheme.onSurfaceVariant,
                    backgroundColor:
                        Theme.of(context).colorScheme.surfaceBright,
                    title: GestureDetector(
                      child: const Text("Home"),
                      onTap: () {
                        setState(() {
                          setState(() {
                            _tabs.add(_tabs.length);
                          });
                        });
                      },
                    ),
                    actions: <Widget>[
                      IconButton(
                          icon: const Icon(Icons.search),
                          onPressed: () {
                            setState(() {
                              // _isSearchPage = !_isSearchPage;
                            });
                          }),
                    ],
                    snap: true,
                    floating: true,
                  ),
                  SliverPersistentHeader(
                    pinned: true,
                    // floating: true,
                    delegate: _StickyTabBarDelegate(
                        child: TabBar(
                      isScrollable: true,
                      tabAlignment: TabAlignment.start,
                      tabs: _tabs.map((it) {
                        return Tab(text: it.toString());
                      }).toList(),
                    )),
                  ),
                ];
              },
              body: const Text("data"),
            ),
          ),
        ),
      ),
    );
  }
}

class _StickyTabBarDelegate extends SliverPersistentHeaderDelegate {
  final PreferredSizeWidget child;

  _StickyTabBarDelegate({required this.child});

  @override
  Widget build(
      BuildContext context, double shrinkOffset, bool overlapsContent) {
    return Container(
        color: Theme.of(context).colorScheme.surfaceBright, child: child);
  }

  @override
  double get maxExtent => child.preferredSize.height;

  @override
  double get minExtent => child.preferredSize.height;

  @override
  bool shouldRebuild(SliverPersistentHeaderDelegate oldDelegate) {
    return true;
  }
}
