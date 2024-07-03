import 'package:distribute/common/http.dart';
import 'package:distribute/common/result.dart';
import 'package:distribute/components/add/add_body_option_item.dart';
import 'package:distribute/components/add/add_bottom_sheet_item.dart';
import 'package:distribute/models/search_item.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class AddPage extends StatefulWidget {
  const AddPage({super.key});

  @override
  State<StatefulWidget> createState() => _AddPageState();
}

class _AddPageState extends State<AddPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        surfaceTintColor: Theme.of(context).colorScheme.onSecondary,
        foregroundColor: Theme.of(context).colorScheme.onSurfaceVariant,
        backgroundColor: Theme.of(context).colorScheme.surfaceBright,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        title: const Text("新建联系"),
      ),
      floatingActionButton: FloatingActionButton(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(50)),
        onPressed: () {
          showModalBottomSheet(
            context: context,
            builder: (BuildContext context) {
              return Column(
                children: [
                  SizedBox(
                    height: 40,
                    child: Center(
                      child: Container(
                        height: 5,
                        width: 100,
                        color: Theme.of(context).colorScheme.onSurface,
                      ),
                    ),
                  ),
                  buildSearchInput(),
                  buildBottomSheetContent(),
                ],
              );
            },
          );
        },
        child: const Icon(Icons.person),
      ),
      body: buildBody(),
    );
  }

  Widget buildBody() {
    return const Column(
      children: [
        AddBodyOptionItem(
          icon: Icons.account_tree_sharp,
          text: "新建群组",
        ),
        AddBodyOptionItem(
          icon: Icons.accessibility_outlined,
          text: "匿名通信",
        ),
      ],
    );
  }

  late TextEditingController _controller;

  Widget buildSearchInput() {
    return Container(
      color: Theme.of(context).colorScheme.surface,
      child: Row(
        children: [
          Expanded(
            child: TextField(
              controller: _controller,
              autofocus: true,
              style: TextStyle(
                fontSize: 16,
                color: Theme.of(context).colorScheme.secondary,
              ),
              maxLines: 6,
              minLines: 1,
              onChanged: (text) {
                setState(() {});
              },
              onSubmitted: (text) {},
              textInputAction: TextInputAction.done,
              decoration: InputDecoration(
                hintText: "输入关键词...",
                border: InputBorder.none,
                fillColor: Theme.of(context).colorScheme.surface,
                filled: true,
                isCollapsed: true,
                contentPadding:
                    const EdgeInsets.symmetric(horizontal: 8, vertical: 10),
              ),
            ),
          ),
          IconButton(
            onPressed: onSearch,
            icon: Icon(
              Icons.search,
              color: Theme.of(context).colorScheme.primary,
            ),
            iconSize: 28,
          )
        ],
      ),
    );
  }

  void onSearch() {
    Http.post("/search/all", {"title": _controller.text}).then((res) {
      Result<List<SearchItem>> result = Result.fromJsonT(res, (json) {
        return (json as List<dynamic>)
            .map((e) => SearchItem.fromJson(e as Map<String, dynamic>))
            .toList();
      });
      if (result.status == true && result.data != null) {
        _items = result.data!;
      }
    });
  }

  late List<SearchItem> _items;

  @override
  void initState() {
    _controller = TextEditingController();
    _items = [];
    super.initState();
  }

  Widget buildBottomSheetContent() {
    return Expanded(
      child: DefaultTabController(
        length: 2,
        child: Column(
          children: [
            const TabBar(
              tabs: [
                Tab(text: "用户"),
                Tab(text: "群组"),
              ],
            ),
            Expanded(
              child: TabBarView(
                children: [
                  buildPage(false),
                  buildPage(true),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildPage(bool type) {
    Iterable<SearchItem> items = _items.where((it) => it.type == type);
    if (items.isNotEmpty) {
      return ListView(
        children: items.map((it) => AddBottomSheetItem(it)).toList(),
      );
    } else {
      return const Center(
        child: Text("无"),
      );
    }
  }
}
