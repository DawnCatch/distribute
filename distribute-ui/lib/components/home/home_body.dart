import 'package:animations/animations.dart';
import 'package:distribute/components/home/home_body_relation.dart';
import 'package:distribute/components/home/home_body_search.dart';
import 'package:flutter/material.dart';

class HomeBody extends StatefulWidget {
  const HomeBody({super.key});

  @override
  State<StatefulWidget> createState() => _HomeBodyState();
}

class _HomeBodyState extends State<HomeBody> with TickerProviderStateMixin {
  late bool _isSearchPage;
  late List<String> _tabs;
  late List<Widget> _pages;

  late FocusNode _searchFocusNode;

  late AnimationController _controller;
  late Animation<double> _leadingIconAnimation;
  late Animation<double> _searchIconAnimation;

  @override
  void initState() {
    _isSearchPage = false;
    _tabs = ["1", "2", "3"];
    _pages = [const HomeBodyRelation(), const HomeBodySearch()];
    _searchFocusNode = FocusNode();
    _controller = AnimationController(
        duration: const Duration(milliseconds: 200), vsync: this);
    _leadingIconAnimation = Tween(begin: 0.0, end: 1.0).animate(_controller);
    _searchIconAnimation = Tween(begin: 1.0, end: 0.0).animate(_controller);
    super.initState();
  }

  Widget buildTitle() {
    return AnimatedSwitcher(
      duration: const Duration(milliseconds: 200),
      child: _isSearchPage
          ? SizedBox(
              height: 20,
              child: TextField(
                focusNode: _searchFocusNode,
                onChanged: (value) => setState(() {}),
                decoration: const InputDecoration(
                  hintText: "搜索",
                  border: InputBorder.none,
                ),
              ),
            )
          : const Text("Distribute"),
    );
  }

  void onLeadingPressed() {
    if (_isSearchPage) {
      setState(() {
        _isSearchPage = false;
      });
      _controller.reverse();
    } else {
      Scaffold.of(context).openDrawer();
    }
  }

  void onSearchPressed() {
    if (_isSearchPage) return;
    setState(() {
      _isSearchPage = true;
    });
    _controller.forward();
    Future.delayed(const Duration(milliseconds: 100), () {
      FocusScope.of(context).requestFocus(_searchFocusNode);
    });
  }

  List<Widget> buildHeader() {
    List<Widget> slivers = [
      SliverAppBar(
        surfaceTintColor: Theme.of(context).colorScheme.onSecondary,
        foregroundColor: Theme.of(context).colorScheme.onSurfaceVariant,
        backgroundColor: Theme.of(context).colorScheme.surfaceBright,
        leading: IconButton(
          icon: AnimatedIcon(
              icon: AnimatedIcons.menu_arrow, progress: _leadingIconAnimation),
          onPressed: onLeadingPressed,
        ),
        title: buildTitle(),
        actions: <Widget>[
          FadeTransition(
            opacity: _searchIconAnimation,
            child: IconButton(
              icon: const Icon(Icons.search),
              onPressed: onSearchPressed,
            ),
          ),
        ],
        snap: true,
        floating: true,
        pinned: _isSearchPage,
      ),
    ];
    if (_tabs.isNotEmpty) {
      slivers.add(
        SliverPersistentHeader(
          pinned: true,
          delegate: _StickyTabBarDelegate(
            child: _isSearchPage
                ? TabBar(
                    key: ValueKey<bool>(_isSearchPage),
                    isScrollable: true,
                    tabAlignment: TabAlignment.start,
                    tabs: _tabs.map((it) {
                      return Tab(text: it.toString());
                    }).toList(),
                  )
                : TabBar(
                    key: ValueKey<bool>(_isSearchPage),
                    isScrollable: true,
                    tabAlignment: TabAlignment.start,
                    tabs: const [
                      Tab(text: "data"),
                      Tab(text: "data"),
                      Tab(text: "data")
                    ],
                  ),
          ),
        ),
      );
    }
    return slivers;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Theme.of(context).colorScheme.surfaceBright,
      child: SafeArea(
        child: DefaultTabController(
          length: _tabs.length,
          child: NestedScrollView(
            headerSliverBuilder: (context, isScrolled) {
              return buildHeader();
            },
            body: Container(
              height: 200,
              color: Theme.of(context).colorScheme.surface,
              child: PageTransitionSwitcher(
                reverse: _isSearchPage,
                duration: const Duration(milliseconds: 500),
                transitionBuilder: (child, animation, secondaryAnimation) {
                  return SharedAxisTransition(
                    animation: animation,
                    secondaryAnimation: secondaryAnimation,
                    transitionType: SharedAxisTransitionType.horizontal,
                    child: child,
                  );
                },
                child: _pages[_isSearchPage ? 1 : 0],
              ),
            ),
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _searchFocusNode.dispose();
    super.dispose();
  }
}

class _StickyTabBarDelegate extends SliverPersistentHeaderDelegate {
  final PreferredSizeWidget child;

  _StickyTabBarDelegate({required this.child});

  @override
  Widget build(
      BuildContext context, double shrinkOffset, bool overlapsContent) {
    return ClipRect(
      child: AnimatedSwitcher(
        duration: const Duration(milliseconds: 100),
        child: Container(
            key: child.key,
            color: Theme.of(context).colorScheme.surfaceBright,
            child: child),
      ),
    );
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
